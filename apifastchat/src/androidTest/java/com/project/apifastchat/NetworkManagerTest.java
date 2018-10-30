package com.project.apifastchat;

import android.content.Context;
import android.os.ConditionVariable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.project.apifastchat.entity.CodeResp;
import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.mappers.CommonJsonMapper;
import com.project.apifastchat.mappers.UsersJsonMapper;
import com.project.apifastchat.net.INerworkManager;
import com.project.apifastchat.net.NetworkManager;
import com.project.apifastchat.net.TcpClient;
import com.project.apifastchat.requests.ARequest;
import com.project.apifastchat.requests.CheckConnectRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

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
    private Throwable error;
    private int count = 0;
    private int max_count = 1000;
    private final ConditionVariable cv = new ConditionVariable();

    @Before
    public void init(){
        setUp();
    }

    private void setUp(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        networkManager = NetworkManager.getInstance();
        networkManager.setCommLink(new TcpClient(appContext));
        mapper = new CommonJsonMapper();
    }

    @Test
    public void OneRequestTest(){
        count = 0;
        networkManager.executeRequest(new CheckConnectRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        CommonMsg msg = mapper.deserialize(value, CommonMsg.class);
                        Log.d("NetworkManager", msg.toString());
                        count++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        error = e;
                        cv.open();
                    }

                    @Override
                    public void onComplete() {
                        cv.open();
                    }
                });
        blockOnEvent();
        checkOnError();
        assertTrue("Запрос не выполнен", count == 1);
    }

    @Test
    public void ListRequestTest(){
        count = 0;
        List<ARequest> list = new ArrayList<>(max_count);
        for(int i = 0; i < max_count; i++){
            list.add(new CheckConnectRequest());
        }
        networkManager.executeRequest(list)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        CommonMsg msg = mapper.deserialize(value, CommonMsg.class);
                        if (msg.getCode_resp() == CodeResp.Success) count++;
                        Log.d("NetworkManager", msg.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        cv.open();
                    }

                    @Override
                    public void onComplete() {
                        cv.open();
                    }
                });
        blockOnEvent();
        checkOnError();
        assertTrue("Запросы не выполнены", count == max_count);
    }

    protected void checkOnError(){
        assertFalse(error == null ? "" : error.getMessage(), error != null);
    }

    protected void blockOnEvent(){
        assertFalse("Таймаут приема данных", !cv.block(35000));
    }
}
