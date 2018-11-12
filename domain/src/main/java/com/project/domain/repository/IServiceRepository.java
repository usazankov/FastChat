package com.project.domain.repository;

import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.net.INetworkManager;
import com.project.apifastchat.requests.AuthRequest;

import io.reactivex.Observable;

public interface IServiceRepository {
    Observable<AuthRespEntity> auth(AuthRequest request);
    Observable<INetworkManager.ConnectState> getConnectState();
}
