package com.project.apifastchat.serializers;

import java.lang.reflect.Type;

public interface ISerializer {

    /**
     * Serialize an object to Json.
     *
     * @param object to serialize.
     */
    String serialize(Object object, Type clazz);

    /**
     * Deserialize a json representation of an object.
     *
     * @param string A json string to deserialize.
     */
    <T> T deserialize(String string, Type clazz);
}
