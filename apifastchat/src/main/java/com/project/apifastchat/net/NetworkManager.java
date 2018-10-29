package com.project.apifastchat.net;

import android.os.SystemClock;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.exceptions.NetworkException;
import com.project.apifastchat.mappers.CommonJsonMapper;
import com.project.apifastchat.requests.ARequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class NetworkManager implements INerworkManager{

    private static IEventListener eventListener;
    private static ICommLink commLink;
    private static CommonJsonMapper mapper;
    private static final Map<String, String> hashResp = new HashMap<>();
    private static IConnectStateListener stateListener;
    private static ConnectState currentState = ConnectState.OFFLINE;
    private static Thread thread;
    static ICommLink.ICommLinkListener listener = new ICommLink.ICommLinkListener() {
        @Override
        public void messageReceived(String message) {
            CommonMsg msg = mapper.deserialize(message, CommonMsg.class);
            if(msg.getCommand_obj() != null && msg.getCommand_obj().getCommandId() != null){
                synchronized (hashResp){
                    hashResp.put(msg.getId(), message);
                }
            }else if(msg.getEvent_obj() != null && msg.getEvent_obj().getEventId() != null){
                eventListener.onEvent(msg.getEvent_obj().getEventId(), message);
            }
        }

        @Override
        public void onConnect() {
            stateListener.onChangeState(ConnectState.ONLINE, currentState);
            currentState = ConnectState.ONLINE;
        }

        @Override
        public void onDisconnect() {
            stateListener.onChangeState(ConnectState.OFFLINE, currentState);
            currentState = ConnectState.OFFLINE;
        }

        @Override
        public void onError(Throwable e) {

        }
    };

    public NetworkManager(ICommLink comm){
        commLink = comm;
        commLink.setCommLinkListener(listener);
        mapper = new CommonJsonMapper();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    commLink.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Observable<String> executeRequest(final ARequest request) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                boolean isConnect;
                synchronized (commLink) {
                    String req = request.createRequest();
                    isConnect = commLink.isConnected();
                    if(isConnect)commLink.send(req);
                }
                if (!isConnect) {
                    e.onError(new NetworkException());
                    return;
                }

                //SystemClock.sleep(2000);
                String resp = "";
                while (true) {
                    synchronized (hashResp) {
                        if (!hashResp.containsKey(request.getMsgId())) continue;
                        resp = hashResp.get(request.getMsgId());
                        hashResp.remove(request.getMsgId());
                    }
                    e.onNext(resp);
                    e.onComplete();
                    return;
                }
                //e.onError(new NetworkException());
            }
        });
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void start() {
        thread.start();
    }

    @Override
    public void setEventListener(IEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void setConnectStateListener(IConnectStateListener connectStateListener) {
        this.stateListener = connectStateListener;
    }
}
