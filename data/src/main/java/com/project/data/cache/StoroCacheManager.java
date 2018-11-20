package com.project.data.cache;

import java.io.File;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import st.lowlevel.storo.Storo;
import st.lowlevel.storo.StoroBuilder;

public class StoroCacheManager implements ICacheManager{
    public StoroCacheManager(File cacheDir, long maxSize){
        StoroBuilder.configure(maxSize)  // maximum size to allocate in bytes
                .setCacheDirectory(cacheDir)
                .initialize();
    }

    @Override
    public <T> boolean put(String key, T obj) {
        return Storo.put(key, obj).execute();
    }

    @Override
    public <T> boolean put(String key, T obj, long duration, TimeUnit timeUnit) {
        return Storo.put(key, obj).setExpiry(duration, timeUnit).execute();
    }

    @Override
    public <T> T get(String key, Type classOfT) {
        T obj = null;
        try {
            obj = (T)Storo.get(key, classOfT).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public boolean isExpired(String key) {
        return Storo.hasExpired(key).execute();
    }

    @Override
    public void delete(String key) {
        Storo.delete(key);
    }

    @Override
    public boolean contains(String key) {
        return Storo.contains(key);
    }
}
