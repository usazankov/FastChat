package com.project.apifastchat.requests;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.mappers.CommonJsonMapper;

public abstract class ARequest {

    private String msgId;

    CommonJsonMapper jsonMapper;

    public ARequest(){
        jsonMapper = new CommonJsonMapper();
    }

    public abstract String createRequest();

    public String getMsgId() {
        return msgId;
    }

    private String generateUniqId(CommonMsg msg){
        return "111";
    }

    public String toDataRequest(Object object, Class clazz){
        if(object instanceof CommonMsg){
            msgId = generateUniqId((CommonMsg)object);
            ((CommonMsg)object).setId(msgId);
        }
        return jsonMapper.serialize(object, clazz);
    }
}
