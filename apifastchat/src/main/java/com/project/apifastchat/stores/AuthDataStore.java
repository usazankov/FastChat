package com.project.apifastchat.stores;

import com.project.apifastchat.mappers.AuthRespJsonMapper;
import com.project.apifastchat.net.ICommLink;
import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.net.INerworkManager;
import com.project.apifastchat.net.NetworkManager;
import com.project.apifastchat.requests.AuthRequest;
import com.project.apifastchat.stores.interfaces.IAuthDataStore;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthDataStore extends CommonDataStore implements IAuthDataStore{
    private AuthRespJsonMapper mapper;
    public AuthDataStore(INerworkManager nerworkManager, AuthRespJsonMapper mapper){
        super(nerworkManager);
        this.mapper = mapper;
    }

    @Override
    public Observable<AuthRespEntity> doAuthentication(AuthRequest authRequest) {
        return networkManager.executeRequest(authRequest)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, AuthRespEntity>() {
                    @Override
                    public AuthRespEntity apply(String s) throws Exception {
                        return mapper.getAuthResponse(s);
                    }
                });
    }
}
