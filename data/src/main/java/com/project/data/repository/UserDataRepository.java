package com.project.data.repository;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.requests.UserInfoRequest;
import com.project.apifastchat.requests.UserListRequest;
import com.project.apifastchat.stores.UserDataStore;
import com.project.apifastchat.stores.interfaces.IUserDataStore;
import com.project.data.repository.datasource.UserDataStoreFactory;
import com.project.domain.repository.IUserRepository;

import java.util.List;

import io.reactivex.Observable;

public class UserDataRepository implements IUserRepository{
    private UserDataStoreFactory userDataStoreFactory;

    public UserDataRepository(UserDataStoreFactory userDataStoreFactory){
        this.userDataStoreFactory = userDataStoreFactory;
    }

    @Override
    public Observable<List<User>> getUserList(UserListRequest request) {
        IUserDataStore userDataStore = userDataStoreFactory.createCloudDataStore();
        return userDataStore.getUsersList(request);
    }

    @Override
    public Observable<User> getUserInfo(UserInfoRequest request) {
        IUserDataStore userDataStore = userDataStoreFactory.create(request.getUserInfoId());
        return userDataStore.getUserInfo(request);
    }
}
