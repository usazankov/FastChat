package com.project.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.project.apifastchat.mappers.UsersJsonMapper;
import com.project.apifastchat.net.INetworkManager;
import com.project.apifastchat.stores.UserDataStore;
import com.project.apifastchat.stores.interfaces.IUserDataStore;
import com.project.data.cache.IUserCache;

public class UserDataStoreFactory {
    private final Context context;
    private final IUserCache userCache;
    private INetworkManager networkManager;

    UserDataStoreFactory(@NonNull Context context, @NonNull IUserCache userCache, INetworkManager networkManager) {
        this.context = context.getApplicationContext();
        this.userCache = userCache;
        this.networkManager = networkManager;
    }

    /**
     * Create {@link IUserDataStore} from a user id.
     */
    public IUserDataStore create(final String userId) {
        IUserDataStore userDataStore;

        if (!this.userCache.isExpired() && this.userCache.isCached(userId)) {
            userDataStore = new DiskUserDataStore(this.userCache);
        } else {
            userDataStore = createCloudDataStore();
        }

        return userDataStore;
    }

    /**
     * Create {@link IUserDataStore} to retrieve data from the Cloud.
     */
    public IUserDataStore createCloudDataStore() {
        final UsersJsonMapper userEntityJsonMapper = new UsersJsonMapper();
        return new UserDataStore(networkManager, userEntityJsonMapper);
    }
}
