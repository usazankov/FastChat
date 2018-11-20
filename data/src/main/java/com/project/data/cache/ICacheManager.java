package com.project.data.cache;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public interface ICacheManager {
    <T> boolean put(String key, T obj);
    <T> boolean put(String key, T obj, long duration, TimeUnit timeUnit);
    <T> T get(String key, Type classOfT);
    boolean isExpired(String key);
    void delete(String key);
    boolean contains(String key);
}
