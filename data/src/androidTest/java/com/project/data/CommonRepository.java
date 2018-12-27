package com.project.data;

import android.content.Context;
import android.os.ConditionVariable;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;

import com.project.apifastchat.CommonTcp;
import com.project.apifastchat.net.ICommLink;
import com.project.apifastchat.net.INetworkManager;
import com.project.apifastchat.net.NetworkManager;
import com.project.apifastchat.net.TcpClient;
import com.project.apifastchat.requests.ARequest;

import java.io.File;

import static org.junit.Assert.assertFalse;

public class CommonRepository {
    protected Throwable error;
    protected NetworkManager networkManager;
    protected TcpClient client;
    protected final ConditionVariable cv = new ConditionVariable();
    protected Context appContext;
    protected File path;
    public void setUp(){
        appContext = InstrumentationRegistry.getTargetContext();
        client = new TcpClient(appContext);
        networkManager = new NetworkManager(client);
        File rootDir = Environment.getExternalStorageDirectory();
        path = new File(rootDir,"temp/");
    }

    protected void cont(){
        cv.open();
    }

    protected void blockOnEvent(){
        assertFalse("Таймаут приема данных", !cv.block(10000));
    }

    protected void checkOnError(){
        assertFalse(error == null ? "" : error.getMessage(), error != null);
    }

}
