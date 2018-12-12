package com.project.domain.repository;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.requests.UserInfoRequest;
import com.project.apifastchat.requests.UserListRequest;

import java.util.List;

import io.reactivex.Observable;

public interface IUserRepository {
    Observable<List<User>> getUserList(UserListRequest request);
    Observable<User> getUserInfo(UserInfoRequest request);
}
