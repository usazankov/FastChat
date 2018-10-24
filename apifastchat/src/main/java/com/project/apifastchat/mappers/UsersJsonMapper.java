package com.project.apifastchat.mappers;

import com.google.gson.reflect.TypeToken;
import com.project.apifastchat.entity.User;
import com.project.apifastchat.entity.UserList;

import java.lang.reflect.Type;
import java.util.List;

public class UsersJsonMapper extends CommonJsonMapper {

    public List<User> getUsersList(String json){
        UserList list = deserialize(json, UserList.class);
        return list.getUserList();
    }

    public User getUserInfo(String json){
        return deserialize(json, User.class);
    }
}
