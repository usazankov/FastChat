package com.project.apifastchat;

import android.support.test.runner.AndroidJUnit4;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.mappers.AuthRespJsonMapper;
import com.project.apifastchat.mappers.UsersJsonMapper;
import com.project.apifastchat.requests.UserListRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class UsersTest extends CommonTcp {
    private UsersJsonMapper usersJsonMapper;
    private List<User> userList;
    private List<String> testList;
    @Before
    public void init(){
        setUp();
        testList = new ArrayList<>(3);
        testList.add("user1");
        testList.add("user2");
        testList.add("user3");
        usersJsonMapper = new UsersJsonMapper();
    }

    @Test
    public void getUsersListTest(){
        run();
        Assert.assertNotNull("Пустой список пользователей", userList);

        for (User i : userList) {
            assertFalse("Нет пользователя " + i.getId(), !testList.contains(i.getId()));
        }
    }

    private UserListRequest createFakeReqUserList(){
        return UserListRequest.newBuilder().build();
    }

    @Override
    protected boolean onReceiveEvent(String message) {
        userList = usersJsonMapper.getUsersList(message);
        return true;
    }

    @Override
    protected boolean onConnectEvent() {
        sendData(createFakeReqUserList());
        return true;
    }

    @Override
    protected boolean onDisconnectEvent() {
        return true;
    }
}
