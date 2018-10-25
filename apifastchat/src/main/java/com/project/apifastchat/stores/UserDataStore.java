package com.project.apifastchat.stores;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.requests.UserListRequest;
import com.project.apifastchat.stores.interfaces.IUserDataStore;

import java.util.List;

import io.reactivex.Observable;

public class UserDataStore implements IUserDataStore {

    @Override
    public Observable<List<User>> getUsersList(UserListRequest userListRequest) {
        return null;
    }
}
