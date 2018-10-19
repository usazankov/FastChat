package com.project.apifastchat;

import android.content.Context;
import android.os.ConditionVariable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.mappers.AuthRespJsonMapper;
import com.project.apifastchat.requests.AuthRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class AuthTest {
    private static final String USER_ID = "test_user";
    private static final String USER_NAME = "Yuri";
    AuthRespJsonMapper respJsonMapper;
    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        final ConditionVariable cv = new ConditionVariable();
        final TcpClient client = new TcpClient(new ICommLink.ICommLinkListener() {
            @Override
            public void messageReceived(String message) {
                Log.d("Received:", message);
                AuthRespEntity resp = respJsonMapper.deserialize(message, AuthRespEntity.class);
                assertNull("Ответ от хоста: null", resp);
                cv.open();
            }

            @Override
            public void onError(Throwable e) {
                String msg = e.getMessage();
                Assert.assertNotNull(msg);
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                client.run();
                AuthRequest req = createFakeAuthReq();
                try {
                    client.send(req.createRequest());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        assertFalse("Таймаут приема данных", !cv.block(35000));
    }

    public static AuthRequest createFakeAuthReq(){
        return AuthRequest.newBuilder()
                .setUserId(USER_ID)
                .setUserName(USER_NAME)
                .build();
    }
}
