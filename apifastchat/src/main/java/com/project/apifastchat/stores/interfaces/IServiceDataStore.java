package com.project.apifastchat.stores.interfaces;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.requests.CheckConnectRequest;

import io.reactivex.Observable;

public interface IServiceDataStore {
    Observable<CommonMsg> doCheckConnect(CheckConnectRequest checkConnectRequest);
}
