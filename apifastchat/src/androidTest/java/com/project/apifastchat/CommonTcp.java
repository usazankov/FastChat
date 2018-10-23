package com.project.apifastchat;

import android.os.ConditionVariable;
import com.project.apifastchat.requests.ARequest;


import java.io.IOException;

import static org.junit.Assert.assertFalse;

public abstract class CommonTcp {
    protected Throwable e;
    protected TcpClient client;
    protected final ConditionVariable cv = new ConditionVariable();

    public void setUp(){
        client = new TcpClient(new ICommLink.ICommLinkListener() {
            @Override
            public void messageReceived(String message) {
                if(onReceiveEvent(message)) cv.open();
            }

            @Override
            public void onConnect() {
                onConnectEvent();
            }

            @Override
            public void onDisconnect() {
                if(onDisconnectEvent()) cv.open();
            }

            @Override
            public void onError(Throwable e) {
                CommonTcp.this.e = e;
                cv.open();
            }
        });
    }

    protected void run(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                client.run();
            }
        });
        thread.start();
        blockOnEvent();
        checkOnError();
    }

    protected void stop(){
        client.stop();
    }

    protected void sendData(ARequest req){
        try {
            if(client != null) client.send(req.createRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void blockOnEvent(){
        assertFalse("Таймаут приема данных", !cv.block(1000000));
    }

    protected void checkOnError(){
        assertFalse(e == null ? "" : e.getMessage(), e != null);
    }

    protected abstract boolean onReceiveEvent(String message);

    protected abstract boolean onConnectEvent();

    protected abstract boolean onDisconnectEvent();

}
