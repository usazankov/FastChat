package com.project.apifastchat;

import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.entity.CodeResp;
import com.project.apifastchat.entity.Command;
import com.project.apifastchat.mappers.AuthRespJsonMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AuthRespJsonMapperTest {
    private static String jsonAuthResp = "{\n" +
            "    \"Code_resp\": \"c_success\",\n" +
            "    \"Command_obj\": {\n" +
            "        \"Command_id\": \"c_auth_req\"\n" +
            "    },\n" +
            "    \"Date\": \"18.10.2018\",\n" +
            "    \"Time\": \"17:53\",\n" +
            "    \"User_id\": \"test_user\"\n" +
            "}";

    private AuthRespJsonMapper respJsonMapper;

    @Before
    public void setUp(){
        respJsonMapper = new AuthRespJsonMapper();
    }

    @Test
    public void testTransformAuthResp(){
        AuthRespEntity resp = respJsonMapper.getAuthResponse(jsonAuthResp);
        assertThat(resp.getCode_resp(), is(equalTo(CodeResp.Success)));
        assertThat(resp.getCommand_obj().getCommandId(), is(equalTo(Command.CommandId.Auth)));
        assertThat(resp.getDate(), is(equalTo("18.10.2018")));
        assertThat(resp.getTime(), is(equalTo("17:53")));
        assertThat(resp.getUserId(), is(equalTo("test_user")));
    }

}
