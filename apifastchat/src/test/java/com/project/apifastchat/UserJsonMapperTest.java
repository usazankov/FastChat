package com.project.apifastchat;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserJsonMapperTest {
    private static String jsonUsers = "{\n" +
            "    \"Code_resp\": \"c_success\",\n" +
            "    \"Date\": \"24.10.2018\",\n" +
            "    \"Event_obj\": {\n" +
            "        \"Event_id\": \"e_users_list\"\n" +
            "    },\n" +
            "    \"Time\": \"16:27\",\n" +
            "    \"Users_list\": [\n" +
            "    ]\n" +
            "}";

}
