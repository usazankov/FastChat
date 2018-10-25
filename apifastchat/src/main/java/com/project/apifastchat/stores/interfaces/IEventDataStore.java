package com.project.apifastchat.stores.interfaces;

import com.project.apifastchat.entity.User;
import java.util.List;

import io.reactivex.Observable;

public interface IEventDataStore {
    Observable<List<User>> updateUsersList();
}
