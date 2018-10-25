package com.project.apifastchat.stores.interfaces;

import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.requests.AuthRequest;

import io.reactivex.Observable;

public interface IAuthDataStore {
    Observable<AuthRespEntity> doAuthentication(AuthRequest authRequest);
}
