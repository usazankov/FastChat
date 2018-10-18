package com.project.apifastchat.requests;

import com.project.apifastchat.entity.AuthReqEntity;
import com.project.apifastchat.entity.Command;
import com.project.apifastchat.mappers.CommonJsonMapper;

public class AuthRequest extends Request {
    CommonJsonMapper<AuthReqEntity> commonJsonMapper;
    private String userId;
    private String userName;
    private AuthRequest(){
        commonJsonMapper = new CommonJsonMapper<>();
    }

    @Override
    public String createRequest() {
        AuthReqEntity req = new AuthReqEntity();
        Command com = new Command();
        com.setCommandId(Command.CommandId.Auth);
        req.setCommand_obj(com);
        req.setUserId(userId);
        return commonJsonMapper.toJson(req);
    }

    public static Builder newBuilder() {
        return new AuthRequest().new Builder();
    }

    public class Builder{
        private Builder() {
            // private constructor
        }
        public Builder setUserId(String userId){
            AuthRequest.this.userId = userId;
            return this;
        }
        public Builder setUserName(String userName){
            AuthRequest.this.userName = userName;
            return this;
        }
        public AuthRequest build(){
            return AuthRequest.this;
        }
    }
}
