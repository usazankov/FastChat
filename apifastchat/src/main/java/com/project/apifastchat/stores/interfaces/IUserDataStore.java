package com.project.apifastchat.stores.interfaces;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.requests.UserInfoRequest;
import com.project.apifastchat.requests.UserListRequest;

import java.util.List;

import io.reactivex.Observable;

public interface IUserDataStore {

    Observable<List<User>> getUsersList(UserListRequest userListRequest);

    Observable<User> getUserInfo(UserInfoRequest userListRequest);

}
