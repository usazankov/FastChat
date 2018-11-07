package com.project.apifastchat.stores;

import com.project.apifastchat.entity.Event;
import com.project.apifastchat.entity.User;
import com.project.apifastchat.mappers.CommonJsonMapper;
import com.project.apifastchat.net.INetworkManager;
import com.project.apifastchat.stores.interfaces.IEventDataStore;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class EventDataStore extends CommonDataStore implements IEventDataStore{

    private CommonJsonMapper mapper;

    private class EventData{
        Event.EventId eventId;
        String data;
    }

    public EventDataStore(INetworkManager nerworkManager, CommonJsonMapper mapper){
        super(nerworkManager);
        this.mapper = mapper;
    }

    private Observable<EventData> getEvent() {
        return Observable.create(new ObservableOnSubscribe<EventData>() {
            @Override
            public void subscribe(final ObservableEmitter<EventData> e) throws Exception {
                networkManager.setEventListener(new INetworkManager.IEventListener() {

                    @Override
                    public void onEvent(Event.EventId eventId, String data) {
                        EventData eventData = new EventData();
                        eventData.data = data;
                        eventData.eventId = eventId;
                        e.onNext(eventData);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .share();
    }

    @Override
    public Observable<INetworkManager.ConnectState> getConnectState(){
        return Observable.create(new ObservableOnSubscribe<INetworkManager.ConnectState>() {
            @Override
            public void subscribe(final ObservableEmitter<INetworkManager.ConnectState> e) throws Exception {
                e.onNext(networkManager.getCurrentState());
                networkManager.setConnectStateListener(new INetworkManager.IConnectStateListener() {
                    @Override
                    public void onChangeState(INetworkManager.ConnectState stateNew) {
                        e.onNext(stateNew);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .share();
    }

    @Override
    public Observable<List<User>> updateUsersList() {
        return getEvent()
                .filter(new Predicate<EventData>() {
                    @Override
                    public boolean test(EventData eventData) throws Exception {
                        if (eventData.eventId == Event.EventId.UsersList) return true;
                        return false;
                    }
                })
                .map(new Function<EventData, List<User>>() {
                    @Override
                    public List<User> apply(EventData eventData) throws Exception {
                        return null;
                    }
                });
    }
}
