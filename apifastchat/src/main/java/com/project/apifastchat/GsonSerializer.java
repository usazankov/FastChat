package com.project.apifastchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonSerializer implements ISerializer{

    private final Gson gson;

    public GsonSerializer() {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    /**
     * Serialize an object to Json.
     *
     * @param object to serialize.
     */
    public synchronized String serialize(Object object, Type clazz) {
        return gson.toJson(object, clazz);
    }

    /**
     * Deserialize a json representation of an object.
     *
     * @param string A json string to deserialize.
     */
    public synchronized <T> T deserialize(String string, Type clazz) {
        return gson.fromJson(string, clazz);
    }
}
