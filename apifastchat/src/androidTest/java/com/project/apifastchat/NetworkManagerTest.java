package com.project.apifastchat;

import android.os.ConditionVariable;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.mappers.CommonJsonMapper;
import com.project.apifastchat.mappers.UsersJsonMapper;
import com.project.apifastchat.net.INerworkManager;
import com.project.apifastchat.net.NetworkManager;
import com.project.apifastchat.net.TcpClient;
import com.project.apifastchat.requests.CheckConnectRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class NetworkManagerTest {
    private INerworkManager networkManager;
    private CommonJsonMapper mapper;
    int count = 0;
    int max_count = 1000;
    protected final ConditionVariable cv = new ConditionVariable();
    @Before
    public void init(){
        setUp();

    }

    private void setUp(){
        networkManager = new NetworkManager(new TcpClient());
        mapper = new CommonJsonMapper();
    }

    @Test
    public void NetworkManagerTest(){
        INerworkManager.IConnectStateListener stateListener = new INerworkManager.IConnectStateListener() {
            @Override
            public void onChangeState(INerworkManager.ConnectState stateNew, INerworkManager.ConnectState stateOld) {
                if(stateNew == INerworkManager.ConnectState.ONLINE){
                    for(int i = 0; i < max_count; i++)
                    networkManager.executeRequest(new CheckConnectRequest())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(String value) {
                                    CommonMsg msg = mapper.deserialize(value, CommonMsg.class);
                                    Log.d("NetworkManager", msg.toString());
                                    count++;
                                    if(count == max_count) cv.open();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    cv.open();
                                }

                                @Override
                                public void onComplete() {
                                    //cv.open();
                                }
                            });

                }
            }
        };
        networkManager.setConnectStateListener(stateListener);
        networkManager.start();
        blockOnEvent();
        assertTrue("Количество ответов не равно количеству запросов", count == max_count);
    }

    protected void blockOnEvent(){
        assertFalse("Таймаут приема данных", !cv.block(100000));
    }
}
