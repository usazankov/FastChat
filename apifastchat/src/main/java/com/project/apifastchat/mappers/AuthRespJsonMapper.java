package com.project.apifastchat.mappers;


import com.google.gson.JsonSyntaxException;
import com.project.apifastchat.entity.AuthRespEntity;

public class AuthRespJsonMapper extends CommonJsonMapper<AuthRespEntity> {

    public AuthRespEntity getAuthResponse(String json) throws JsonSyntaxException {
        return fromJson(json);
    }
}
