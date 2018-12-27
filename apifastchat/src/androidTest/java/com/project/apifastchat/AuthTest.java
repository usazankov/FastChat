package com.project.apifastchat;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.entity.CodeResp;
import com.project.apifastchat.mappers.AuthRespJsonMapper;
import com.project.apifastchat.requests.AuthRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
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
        run();
        assertFalse("Не принят ответ от хоста или ошибка десериализации объекта", resp == null);
        if(resp != null){
            assertTrue("Не успешный код ответа хоста на запрос авторизации", resp.getCode_resp().equals(CodeResp.Success));
        }
    }

    private static AuthRequest createFakeAuthReq(){
        return AuthRequest.newBuilder()
                .setUserId(USER_ID)
                .setUserName(USER_NAME)
                .build();
    }

    @Override
    protected boolean onReceiveEvent(String message) {
        resp = respJsonMapper.deserialize(message, AuthRespEntity.class);
        return true;
    }

    @Override
    protected boolean onConnectEvent() {
        AuthRequest req = createFakeAuthReq();
        sendData(req);
        return true;
    }

    @Override
    protected boolean onDisconnectEvent() {
        stop();
        return true;
    }

}
