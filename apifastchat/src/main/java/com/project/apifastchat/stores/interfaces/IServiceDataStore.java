package com.project.apifastchat.stores.interfaces;

import com.project.apifastchat.entity.CommonResp;
import com.project.apifastchat.requests.CheckConnectRequest;

import io.reactivex.Observable;

public interface IServiceDataStore {
    Observable<CommonResp> doCheckConnect(CheckConnectRequest checkConnectRequest);
}
