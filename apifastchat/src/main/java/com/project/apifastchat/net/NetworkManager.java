package com.project.apifastchat.net;

import android.content.Context;
import android.os.ConditionVariable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.project.apifastchat.Consts;
import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.exceptions.NetworkException;
import com.project.apifastchat.mappers.CommonJsonMapper;
import com.project.apifastchat.requests.ARequest;
import com.project.apifastchat.requests.CheckConnectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class NetworkManager implements INerworkManager{
    private IEventListener eventListener;
    private ICommLink commLink;
    private CommonJsonMapper mapper;
    private final Map<String, String> hashResp = new HashMap<>();
    private IConnectStateListener stateListener;
    private ConnectState currentState = ConnectState.OFFLINE;
    private Thread thread;
    private final int timeout_recv = 30000;
    private final int timeout_connect = 30000;
    private Timer timerCheckConnect;

    private ExecutorService backgroundExecutor = Executors.newFixedThreadPool(8, new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = new Thread(runnable, "Background executor service");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setDaemon(true);
            return thread;
        }
    });

    private void runInBackground(final Runnable runnable) {
        backgroundExecutor.submit(runnable);
    }

    private ICommLink.ICommLinkListener listener = new ICommLink.ICommLinkListener() {
        @Override
        public void messageReceived(String message) {
            CommonMsg msg = mapper.deserialize(message, CommonMsg.class);
            if(msg.getCommand_obj() != null && msg.getCommand_obj().getCommandId() != null){
                synchronized (hashResp){
                    if(msg.getId() != null && msg.getId().length() > 0) hashResp.put(msg.getId(), message);
                }
            }else if(msg.getEvent_obj() != null && msg.getEvent_obj().getEventId() != null){
                if(eventListener != null)
                    eventListener.onEvent(msg.getEvent_obj().getEventId(), message);
            }
        }

        @Override
        public void onConnect() {
            currentState = ConnectState.ONLINE;
            if(stateListener != null) stateListener.onChangeState(ConnectState.ONLINE);
        }

        @Override
        public void onDisconnect() {
            currentState = ConnectState.OFFLINE;
            if(stateListener != null) stateListener.onChangeState(ConnectState.OFFLINE);
        }

        @Override
        public void onError(Throwable e) {

        }
    };

    private void send(final ARequest request){
        runInBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (commLink) {
                        commLink.send(request.getDataRequest());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public NetworkManager(ICommLink commLink){
        this.commLink = commLink;
        mapper = new CommonJsonMapper();
        commLink.setCommLinkListener(listener);
        timerCheckConnect = new Timer(true);
        timerCheckConnect.schedule(new TimerTask() {
            @Override
            public void run() {
                connectIfPossible();
            }
        },1000, timeout_connect + 3000);
    }

    private class ProcessingNet implements Runnable{
        private Timer timer;
        private PeriodicSendCheck timerTask;

        @Override
        public void run() {
            try {
                runInBackground(new Runnable() {
                    @Override
                    public void run() {
                        timer = new Timer(true);
                        timerTask = new PeriodicSendCheck();
                        timer.schedule(timerTask, 0, 5000);
                    }
                });
                commLink.run();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(timer != null) timer.cancel();
            }
        }

        class PeriodicSendCheck extends TimerTask{
            @Override
            public void run() {
                if(currentState == ConnectState.ONLINE){
                    ARequest req = new CheckConnectRequest();
                    req.setMsgId(Consts.NO_MSG_ID);
                    send(req);
                }
            }
        }
    }

    private boolean connectIfPossible(){
        if(commLink == null || !commLink.isConnected()) return false;
        if(thread == null || !thread.isAlive()){
            thread = new Thread(new ProcessingNet());
            thread.start();
        }
        long currentTick = System.currentTimeMillis();
        while (currentState != ConnectState.ONLINE){
            if((System.currentTimeMillis() - currentTick) >= timeout_connect){
                return false;
            }
        }
        if(isConnected()) return true;
        return false;
    }

    private boolean isConnected(){
        if(currentState == ConnectState.ONLINE) return true;
        return false;
    }

    private void reset(){
        currentState = ConnectState.OFFLINE;
    }

    @Override
    public Observable<String> executeRequest(final ARequest request) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                boolean flagConnect = isConnected();

                if(!flagConnect)
                    flagConnect = connectIfPossible();

                if (!flagConnect) {
                    e.onError(new NetworkException());
                    return;
                }

                send(request);
                long currentTick = System.currentTimeMillis();
                String resp = "";
                while (true) {
                    if((System.currentTimeMillis() - currentTick) >= timeout_recv){
                        e.onError(new NetworkException());
                        return;
                    }
                    SystemClock.sleep(300);
                    synchronized (hashResp) {
                        if (!hashResp.containsKey(request.getMsgId())) continue;
                        resp = hashResp.get(request.getMsgId());
                        hashResp.remove(request.getMsgId());
                    }
                    e.onNext(resp);
                    e.onComplete();
                    return;
                }
            }
        });
    }

    @Override
    public Observable<String> executeRequest(final List<ARequest> requests) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                boolean flagConnect = isConnected();

                if(!flagConnect)
                    flagConnect = connectIfPossible();

                if (!flagConnect) {
                    e.onError(new NetworkException());
                    return;
                }

                Set<String> hashSet = new HashSet<>();
                for(ARequest req : requests){
                    hashSet.add(req.getMsgId());
                    send(req);
                }

                long currentTick = System.currentTimeMillis();
                while (true) {
                    SystemClock.sleep(500);
                    synchronized (hashResp) {
                        for(Map.Entry<String, String> item : hashResp.entrySet()){
                            if(hashSet.contains(item.getKey())){
                                e.onNext(item.getValue());
                                hashSet.remove(item.getKey());
                            }
                        }
                    }
                    if(hashSet.isEmpty()){
                        synchronized (hashResp) {
                            for (String item : hashSet) {
                                if (hashResp.containsKey(item)) {
                                    hashResp.remove(item);
                                }
                            }
                        }
                        e.onComplete();
                        return;
                    }

                    if((System.currentTimeMillis() - currentTick) >= timeout_recv){
                        synchronized (hashResp) {
                            for (String item : hashSet) {
                                if (hashResp.containsKey(item)) {
                                    hashResp.remove(item);
                                }
                            }
                        }
                        e.onError(new NetworkException());
                        return;
                    }
                }
            }
        });
    }

    @Override
    public ConnectState getCurrentState() {
        return currentState;
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
