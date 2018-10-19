package com.project.apifastchat.mappers;


import com.google.gson.JsonSyntaxException;
import com.project.apifastchat.entity.AuthRespEntity;

public class AuthRespJsonMapper extends CommonJsonMapper {

    public AuthRespEntity getAuthResponse(String json) throws JsonSyntaxException {
        return deserialize(json, AuthRespEntity.class);
    }
}
