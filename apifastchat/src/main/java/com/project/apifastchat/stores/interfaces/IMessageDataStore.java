package com.project.apifastchat.stores.interfaces;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.requests.MessageRequest;

import io.reactivex.Observable;

public interface IMessageDataStore {
    Observable<CommonMsg> sendMsg(MessageRequest request);
}
