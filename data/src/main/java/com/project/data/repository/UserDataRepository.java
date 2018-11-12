package com.project.data.repository;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.requests.UserListRequest;
import com.project.apifastchat.stores.UserDataStore;
import com.project.domain.repository.IUserRepository;

import java.util.List;

import io.reactivex.Observable;

public class UserDataRepository implements IUserRepository{
    private UserDataStore cloudDataStore;

    @Override
    public Observable<List<User>> getUserList(UserListRequest request) {
        return null;
    }

    @Override
    public Observable<List<User>> getUserListByEvent() {
        return null;
    }

    @Override
    public Observable<User> getUserInfo() {
        return null;
    }
}
