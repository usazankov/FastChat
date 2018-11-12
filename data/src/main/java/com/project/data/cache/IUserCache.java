package com.project.data.cache;

import com.project.apifastchat.entity.User;

import io.reactivex.Observable;

public interface IUserCache {

    /**
     * Gets an {@link Observable} which will emit a {@link User}.
     *
     * @param userId The user id to retrieve data.
     */
    Observable<User> get(final String userId);

    /**
     * Puts and element into the cache.
     *
     * @param userEntity Element to insert in the cache.
     */
    void put(User userEntity);

    /**
     * Checks if an element (User) exists in the cache.
     *
     * @param userId The id used to look for inside the cache.
     * @return true if the element is cached, otherwise false.
     */
    boolean isCached(final String userId);

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    boolean isExpired();

    /**
     * Evict all elements of the cache.
     */
    void evictAll();
}
