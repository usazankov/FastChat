package com.project.apifastchat.mappers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.project.apifastchat.entity.AuthRespEntity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CommonJsonMapper<T> {
    private Gson gson;
    private Class<T> persistentClass;
    public CommonJsonMapper(){
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T fromJson(String json) throws JsonSyntaxException {

        TypeToken<T> typeToken = new TypeToken<T>() { };
        Type type = typeToken.getType();
        return gson.fromJson(json, type);
    }

    public String toJson(T obj) throws JsonSyntaxException{
        return gson.toJson(obj);
    }
}
