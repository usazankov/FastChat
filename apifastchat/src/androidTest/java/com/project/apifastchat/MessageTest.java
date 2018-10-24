package com.project.apifastchat;

import android.support.test.runner.AndroidJUnit4;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.mappers.CommonJsonMapper;
import com.project.apifastchat.requests.MessageRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class MessageTest extends CommonTcp {
    CommonJsonMapper mapper;
    CommonMsg resp;
    @Before
    public void init(){
        setUp();
        mapper = new CommonJsonMapper();
    }

    @Test
    public void getUsersListTest(){
        run();
        assertFalse("Нет кода ответа или код ответа не успешный", !resp.getCodeResp().equals("c_success"));

    }

    private MessageRequest createMessageRequest(){
        return MessageRequest.newBuilder()
                .setUserIdFrom("test_user")
                .setMessageBody("Hello world!")
                .setTimeMessage("18:31")
                .setDateMessage("24.10.2018")
                .build();
    }

    @Override
    protected boolean onReceiveEvent(String message) {
        resp = mapper.deserialize(message, CommonMsg.class);
        return true;
    }

    @Override
    protected boolean onConnectEvent() {
        sendData(createMessageRequest());
        return true;
    }

    @Override
    protected boolean onDisconnectEvent() {
        return true;
    }
}
