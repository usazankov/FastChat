package com.project.apifastchat;

import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.requests.AuthRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class AuthMsgTest {
    private static String jsonAuthReq = "{\n" +
            "    \"Command_obj\": {\n" +
            "        \"Command_id\": \"c_auth_req\"\n" +
            "    },\n" +
            "    \"User_id\": \"test_user\"\n" +
            "}";

    private static final String USER_ID = "test_user";
    private static final String USER_NAME = "Yuri";

    private AuthRequest createFakeAuthReq(){
        return AuthRequest.newBuilder()
                .setUserId(USER_ID)
                .setUserName(USER_NAME)
                .build();
    }

    @Test
    public void testCreateRequest(){
        AuthRequest req = createFakeAuthReq();
        String str = req.createRequest();
        str = str.replace(" ", "");
        str = str.replace("\n", "");
        jsonAuthReq = jsonAuthReq.replace(" ", "");
        jsonAuthReq = jsonAuthReq.replace("\n", "");
        assertThat(str, is(equalTo(jsonAuthReq)));
        AuthRespEntity res;
    }
}
