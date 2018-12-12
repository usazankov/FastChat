package com.project.data.repository.datasource;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.requests.UserInfoRequest;
import com.project.apifastchat.requests.UserListRequest;
import com.project.apifastchat.stores.interfaces.IUserDataStore;
import com.project.data.cache.ICacheManager;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class DiskUserDataStore implements IUserDataStore{
    private ICacheManager cacheManager;
    public DiskUserDataStore(ICacheManager cacheManager){
        this.cacheManager = cacheManager;
    }

    @Override
    public Observable<List<User>> getUsersList(UserListRequest userListRequest) {
        throw new UnsupportedOperationException("Operation is not available!!!");
    }

    @Override
    public Observable<User> getUserInfo(final UserInfoRequest userListRequest) {
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                User user = cacheManager.get("test_user", User.class);
                if (user != null) {
                    e.onNext(user);
                    e.onComplete();
                } else {
                    e.onError(new RuntimeException());
                }
            }
        });
    }
}
