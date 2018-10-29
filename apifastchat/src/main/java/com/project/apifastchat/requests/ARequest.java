package com.project.apifastchat.requests;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.mappers.CommonJsonMapper;

import java.util.Random;

public abstract class ARequest {

    private String msgId;
    private static final int MAX_RANDOM = Integer.MAX_VALUE;
    CommonJsonMapper jsonMapper;

    public ARequest(){
        jsonMapper = new CommonJsonMapper();
    }

    public abstract String createRequest();

    public String getMsgId() {
        return msgId;
    }

    private String generateUniqId(CommonMsg msg){
        Random r = new Random();
        int i = r.nextInt(MAX_RANDOM);
        return String.valueOf(i);
    }

    public String toDataRequest(Object object, Class clazz){
        if(object instanceof CommonMsg){
            msgId = generateUniqId((CommonMsg)object);
            ((CommonMsg)object).setId(msgId);
        }
        return jsonMapper.serialize(object, clazz);
    }
}
