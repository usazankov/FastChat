package com.project.apifastchat;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.mappers.AuthRespJsonMapper;
import com.project.apifastchat.requests.AuthRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class AuthTest extends CommonTcp{
    private static final String USER_ID = "test_user";
    private static final String USER_NAME = "Yuri";
    private AuthRespJsonMapper respJsonMapper;
    private AuthRespEntity resp;

    @Before
    public void init(){
        setUp();
        respJsonMapper = new AuthRespJsonMapper();
    }

    @Test
    public void AuthRequestTest() throws Exception {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                client.run();
            }
        });
        thread.start();
        blockOnEvent();
        checkOnError();
        assertFalse("Не принят ответ от хоста или ошибка десериализации объекта", resp == null);
        if(resp != null){
            assertFalse("Не успешный код ответа хоста на запрос авторизации", resp.getCodeResp() == "c_success");
        }
    }

    public static AuthRequest createFakeAuthReq(){
        return AuthRequest.newBuilder()
                .setUserId(USER_ID)
                .setUserName(USER_NAME)
                .build();
    }

    @Override
    protected void onReceiveEvent(String message) {
        super.onReceiveEvent(message);
        resp = respJsonMapper.deserialize(message, AuthRespEntity.class);
    }

    @Override
    protected void onConnectEvent() {
        super.onConnectEvent();
        AuthRequest req = createFakeAuthReq();
        sendData(req);
    }

    @Override
    protected void onDisconnectEvent() {
        super.onDisconnectEvent();
        Log.d("AUTH", "disconnect");
    }

    @Override
    protected void onErrorEvent(Throwable e) {
        super.onErrorEvent(e);
        AuthTest.this.e = e;
    }
}
