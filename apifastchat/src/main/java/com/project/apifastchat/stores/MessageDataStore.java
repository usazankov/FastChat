package com.project.apifastchat.stores;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.mappers.MessagesJsonMapper;
import com.project.apifastchat.net.INetworkManager;
import com.project.apifastchat.requests.MessageRequest;
import com.project.apifastchat.stores.interfaces.IMessageDataStore;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MessageDataStore extends CommonDataStore implements IMessageDataStore{
    private MessagesJsonMapper mapper;
    public MessageDataStore(INetworkManager networkManager, MessagesJsonMapper mapper){
        super(networkManager);
        this.mapper = mapper;
    }

    @Override
    public Observable<CommonMsg> sendMsg(MessageRequest request) {
        return networkManager.executeRequest(request)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, CommonMsg>() {
                    @Override
                    public CommonMsg apply(String s) throws Exception {
                        return mapper.deserialize(s, CommonMsg.class);
                    }
                });
    }
}
