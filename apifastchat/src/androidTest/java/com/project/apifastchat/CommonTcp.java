package com.project.apifastchat;

import android.os.ConditionVariable;
import android.util.Log;

import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.requests.ARequest;
import com.project.apifastchat.requests.AuthRequest;

import org.junit.Before;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class CommonTcp {
    protected Throwable e;
    protected TcpClient client;
    protected final ConditionVariable cv = new ConditionVariable();

    public void setUp(){
        client = new TcpClient(new ICommLink.ICommLinkListener() {
            @Override
            public void messageReceived(String message) {
                onReceiveEvent(message);
                cv.open();
            }

            @Override
            public void onConnect() {
                onConnectEvent();
            }

            @Override
            public void onDisconnect() {
                onDisconnectEvent();
                cv.open();
            }

            @Override
            public void onError(Throwable e) {
                onErrorEvent(e);
                cv.open();
            }
        });
    }

    protected void sendData(ARequest req){
        try {
            if(client != null) client.send(req.createRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void blockOnEvent(){
        assertFalse("Таймаут приема данных", !cv.block(35000));
    }

    protected void checkOnError(){
        assertFalse(e == null ? "" : e.getMessage(), e != null);
    }

    protected void onReceiveEvent(String message){

    }

    protected void onConnectEvent(){

    }

    protected void onDisconnectEvent(){

    }

    protected void onErrorEvent(Throwable e){

    }
}
