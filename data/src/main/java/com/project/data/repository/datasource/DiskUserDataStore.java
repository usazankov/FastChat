package com.project.data.repository.datasource;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.requests.UserListRequest;
import com.project.apifastchat.stores.interfaces.IUserDataStore;
import com.project.data.cache.IUserCache;

import java.util.List;

import io.reactivex.Observable;

public class DiskUserDataStore implements IUserDataStore{
    private IUserCache userCache;
    public DiskUserDataStore(IUserCache userCache){
        this.userCache = userCache;
    }

    @Override
    public Observable<List<User>> getUsersList(UserListRequest userListRequest) {
        throw new UnsupportedOperationException("Operation is not available!!!");
    }

    @Override
    public Observable<User> getUserInfo(UserListRequest userListRequest) {
        return this.userCache.get("test_user");
    }
}
