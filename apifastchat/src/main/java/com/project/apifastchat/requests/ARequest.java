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
    private String data;
    public ARequest(){
        jsonMapper = new CommonJsonMapper();
        data = createRequest();
    }

    protected abstract String createRequest();

    public String getDataRequest(){
        return data;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId){
        this.msgId = msgId;
        data = createRequest();
    }

    private String generateUniqId(CommonMsg msg){
        Random r = new Random();
        int i = r.nextInt(MAX_RANDOM);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSS");
        String currentDateandTime = sdf.format(new Date());
        return String.valueOf(i) + currentDateandTime;
    }

    protected String toDataRequest(Object object, Class clazz){
        if(object instanceof CommonMsg){
            if(msgId == null || msgId.length() == 0){
                msgId = generateUniqId((CommonMsg)object);
            }
            ((CommonMsg)object).setId(msgId);
        }
        return jsonMapper.serialize(object, clazz);
    }
}
