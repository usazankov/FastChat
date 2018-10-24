package com.project.apifastchat.mappers;

import com.project.apifastchat.GsonSerializer;
import com.project.apifastchat.ISerializer;

import java.lang.reflect.Type;

public class CommonJsonMapper {

    private static ISerializer serializer = new GsonSerializer();

    public CommonJsonMapper(){
    }

    public String serialize(Object object, Type clazz) {
        return serializer.serialize(object, clazz);
    }

    public <T> T deserialize(String string, Type clazz) {
        return serializer.deserialize(string, clazz);
    }
}
