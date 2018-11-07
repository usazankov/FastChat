package com.project.apifastchat.requests;

import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.mappers.CommonJsonMapper;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public abstract class ARequest {

    private String msgId;
    private static final int MAX_RANDOM = Integer.MAX_VALUE;
    private CommonJsonMapper jsonMapper;
    public ARequest(){
        jsonMapper = new CommonJsonMapper();
        msgId = generateUniqId();
    }

    protected abstract String createRequest();

    public String getDataRequest(){
        return createRequest();
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId){
        this.msgId = msgId;
    }

    private String generateUniqId(){
        Random r = new Random();
        int i = r.nextInt(MAX_RANDOM);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSS");
        String currentDateandTime = sdf.format(new Date());
        return String.valueOf(i) + currentDateandTime;
    }

    protected String toDataRequest(Object object, Class clazz){
        if(object instanceof CommonMsg){
            ((CommonMsg)object).setId(msgId);
            ((CommonMsg)object).setCode_resp(null);
        }
        return jsonMapper.serialize(object, clazz);
    }
}
