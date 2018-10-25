package com.project.apifastchat.stores;

import com.project.apifastchat.net.ICommLink;
import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.requests.AuthRequest;
import com.project.apifastchat.stores.interfaces.IAuthDataStore;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class AuthDataStore extends CommonDataStore implements IAuthDataStore{

    AuthDataStore(){
        super();
    }

    @Override
    public Observable<AuthRespEntity> doAuthentication(AuthRequest authRequest) {
        return Observable.create(new ObservableOnSubscribe<AuthRespEntity>() {
            @Override
            public void subscribe(ObservableEmitter<AuthRespEntity> e) throws Exception {

            }
        });
    }
}
