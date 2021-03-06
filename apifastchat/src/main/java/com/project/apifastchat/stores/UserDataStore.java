package com.project.apifastchat.stores;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.mappers.UsersJsonMapper;
import com.project.apifastchat.net.INetworkManager;
import com.project.apifastchat.requests.UserInfoRequest;
import com.project.apifastchat.requests.UserListRequest;
import com.project.apifastchat.stores.interfaces.IUserDataStore;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserDataStore extends CommonDataStore implements IUserDataStore {
    private UsersJsonMapper mapper;

    public UserDataStore(INetworkManager nerworkManager, UsersJsonMapper mapper){
        super(nerworkManager);
        this.mapper = mapper;
    }

    @Override
    public Observable<List<User>> getUsersList(UserListRequest userListRequest) {
        return networkManager.executeRequest(userListRequest)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<User>>() {
                    @Override
                    public List<User> apply(String s) throws Exception {
                        return mapper.getUsersList(s);
                    }
                });
    }

    @Override
    public Observable<User> getUserInfo(UserInfoRequest userInfoRequest) {
        return networkManager.executeRequest(userInfoRequest)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, User>() {
                    @Override
                    public User apply(String s) throws Exception {
                        return mapper.getUserInfo(s);
                    }
                });
    }
}
