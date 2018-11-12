package com.project.data.cache;

import android.content.Context;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.serializers.ISerializer;
import com.project.domain.executor.ThreadExecutor;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class UserCacheImpl implements IUserCache{
    private static final String SETTINGS_FILE_NAME = "com.project.fastchat.SETTINGS";
    private static final String SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update";

    private static final String DEFAULT_FILE_NAME = "user_";
    private static final long EXPIRATION_TIME = 60 * 10 * 1000;

    private final Context context;
    private final File cacheDir;
    private final ISerializer serializer;
    private final FileManager fileManager;
    private final ThreadExecutor threadExecutor;

    public UserCacheImpl(Context context, ISerializer serializer,
                         FileManager fileManager, ThreadExecutor executor){
        this.context = context.getApplicationContext();
        this.cacheDir = this.context.getCacheDir();
        this.serializer = serializer;
        this.fileManager = fileManager;
        this.threadExecutor = executor;
    }

    @Override
    public boolean isCached(final String userId) {
        final File userEntityFile = this.buildFile(userId);
        return this.fileManager.exists(userEntityFile);
    }

    @Override
    public Observable<User> get(final String userId) {
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                final File userListFile = UserCacheImpl.this.buildFile(userId);
                final String fileContent = UserCacheImpl.this.fileManager.readFileContent(userListFile);
                final User user = UserCacheImpl.this.serializer.deserialize(fileContent, User.class);
                if (user != null) {
                    e.onNext(user);
                    e.onComplete();
                } else {
                    e.onError(new RuntimeException());
                }
            }
        });
    }

    @Override
    public void put(User user) {
        if (user != null) {
            final File userEntityFile = this.buildFile(user.getId());
            if (!isCached(user.getId())) {
                final String jsonString = this.serializer.serialize(user, User.class);
                this.executeAsynchronously(new CacheWriter(this.fileManager, userEntityFile, jsonString));
                setLastCacheUpdateTimeMillis();
            }
        }
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis();

        boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

        if (expired) {
            this.evictAll();
        }

        return expired;
    }

    @Override
    public void evictAll() {
        this.executeAsynchronously(new CacheEvictor(this.fileManager, this.cacheDir));
    }

    /**
     * Build a file, used to be inserted in the disk cache.
     *
     * @return A valid file.
     */
    private File buildFile(String userId) {
        final StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(this.cacheDir.getPath());
        fileNameBuilder.append(File.separator);
        fileNameBuilder.append(DEFAULT_FILE_NAME);
        fileNameBuilder.append(userId);

        return new File(fileNameBuilder.toString());
    }

    /**
     * Set in millis, the last time the cache was accessed.
     */
    private void setLastCacheUpdateTimeMillis() {
        final long currentMillis = System.currentTimeMillis();
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE, currentMillis);
    }

    /**
     * Get in millis, the last time the cache was accessed.
     */
    private long getLastCacheUpdateTimeMillis() {
        return this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE);
    }

    /**
     * Executes a {@link Runnable} in another Thread.
     *
     * @param runnable {@link Runnable} to execute
     */
    private void executeAsynchronously(Runnable runnable) {
        this.threadExecutor.execute(runnable);
    }

    /**
     * {@link Runnable} class for writing to disk.
     */
    private static class CacheWriter implements Runnable {
        private final FileManager fileManager;
        private final File fileToWrite;
        private final String fileContent;

        CacheWriter(FileManager fileManager, File fileToWrite, String fileContent) {
            this.fileManager = fileManager;
            this.fileToWrite = fileToWrite;
            this.fileContent = fileContent;
        }

        @Override public void run() {
            this.fileManager.writeToFile(fileToWrite, fileContent);
        }
    }

    /**
     * {@link Runnable} class for evicting all the cached files
     */
    private static class CacheEvictor implements Runnable {
        private final FileManager fileManager;
        private final File cacheDir;

        CacheEvictor(FileManager fileManager, File cacheDir) {
            this.fileManager = fileManager;
            this.cacheDir = cacheDir;
        }
        @Override public void run() {
            this.fileManager.clearDirectory(this.cacheDir);
        }
    }
}
