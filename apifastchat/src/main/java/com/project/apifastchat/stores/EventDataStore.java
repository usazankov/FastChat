package com.project.apifastchat.stores;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.entity.Event;
import com.project.apifastchat.entity.User;
import com.project.apifastchat.mappers.CommonJsonMapper;
import com.project.apifastchat.net.INerworkManager;
import com.project.apifastchat.stores.interfaces.IEventDataStore;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class EventDataStore extends CommonDataStore implements IEventDataStore{

    private CommonJsonMapper mapper;

    public EventDataStore(INerworkManager nerworkManager, CommonJsonMapper mapper){
        super(nerworkManager);
        this.mapper = mapper;
    }

    private Observable<CommonMsg> getEvent(){
        return Observable.create(new ObservableOnSubscribe<CommonMsg>() {
            @Override
            public void subscribe(ObservableEmitter<CommonMsg> e) throws Exception {

            }
        });
    }
    Observable<INerworkManager.ConnectState> getConnectState(){
        return Observable.create(new ObservableOnSubscribe<INerworkManager.ConnectState>() {
            @Override
            public void subscribe(final ObservableEmitter<INerworkManager.ConnectState> e) throws Exception {
                e.onNext(nerworkManager.getCurrentState());
                nerworkManager.setConnectStateListener(new INerworkManager.IConnectStateListener() {
                    @Override
                    public void onChangeState(INerworkManager.ConnectState stateNew) {
                        e.onNext(stateNew);
                    }
                });
            }
        });
    }

    @Override
    public Observable<List<User>> updateUsersList() {
        return null;
    }
}
