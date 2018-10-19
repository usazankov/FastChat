package com.project.apifastchat.requests;

import com.project.apifastchat.mappers.CommonJsonMapper;

public abstract class ARequest {

    CommonJsonMapper jsonMapper;

    public ARequest(){
        jsonMapper = new CommonJsonMapper();
    }

    public abstract String createRequest();

    public String toDataRequest(Object object, Class clazz){
        return jsonMapper.serialize(object, clazz);
    }
}
