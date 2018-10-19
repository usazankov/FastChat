package com.project.apifastchat.mappers;

import com.project.apifastchat.GsonSerializer;
import com.project.apifastchat.ISerializer;

public class CommonJsonMapper {

    private static ISerializer serializer = new GsonSerializer();

    public CommonJsonMapper(){
    }

    public String serialize(Object object, Class clazz) {
        return serializer.serialize(object, clazz);
    }

    public <T> T deserialize(String string, Class<T> clazz) {
        return serializer.deserialize(string, clazz);
    }
}
