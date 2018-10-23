package com.project.apifastchat;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.project.apifastchat.entity.CheckConnectEntity;
import com.project.apifastchat.mappers.CommonJsonMapper;
import com.project.apifastchat.requests.CheckConnectRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CheckConnectTest extends CommonTcp{
    CommonJsonMapper mapper;
    int count = 0;
    int max_count = 1000;
    @Before
    public void init(){
        setUp();
        mapper = new CommonJsonMapper();
    }

    @Test
    public void CheckConnectReqTest() {
        run();

    }

    @Override
    protected boolean onReceiveEvent(String message) {
        CheckConnectEntity entity = mapper.deserialize(message, CheckConnectEntity.class);
        if(entity.getCodeResp().equals("c_success")){
            count++;
        }
        Log.d("COUNT", Integer.toString(count));
        if(count == max_count){
            return true;
        }
        return false;
    }

    @Override
    protected boolean onConnectEvent() {
        for(int i = 0; i < max_count; i++)
            sendData(new CheckConnectRequest());
        return true;
    }

    @Override
    protected boolean onDisconnectEvent() {
        stop();
        return true;
    }

}
