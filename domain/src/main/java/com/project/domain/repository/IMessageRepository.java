package com.project.domain.repository;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.entity.Message;
import com.project.apifastchat.requests.MessageRequest;

import io.reactivex.Observable;

public interface IMessageRepository {
    Observable<CommonMsg> sendMessage(MessageRequest request);
    Observable<Message> recvMessage();
}
