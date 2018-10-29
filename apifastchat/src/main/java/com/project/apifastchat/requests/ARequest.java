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
    CommonJsonMapper jsonMapper;

    public ARequest(){
        jsonMapper = new CommonJsonMapper();
    }
    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
    public abstract String createRequest();

    public String getMsgId() {
        return msgId;
    }

    private String generateUniqId(CommonMsg msg){
        Random r = new Random();
        int i = r.nextInt(MAX_RANDOM);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSS");
        String currentDateandTime = sdf.format(new Date());
        String str = String.valueOf(i) + currentDateandTime;
        return str;
    }

    public String toDataRequest(Object object, Class clazz){
        if(object instanceof CommonMsg){
            msgId = generateUniqId((CommonMsg)object);
            ((CommonMsg)object).setId(msgId);
        }
        return jsonMapper.serialize(object, clazz);
    }
}
