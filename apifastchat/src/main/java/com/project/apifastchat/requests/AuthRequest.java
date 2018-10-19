package com.project.apifastchat.requests;

import com.project.apifastchat.entity.AuthReqEntity;
import com.project.apifastchat.entity.Command;

public class AuthRequest extends ARequest {
    private String userId;
    private String userName;
    private AuthRequest(){
    }

    public String createRequest() {
        AuthReqEntity req = new AuthReqEntity();
        Command com = new Command();
        com.setCommandId(Command.CommandId.Auth);
        req.setCommand_obj(com);
        req.setUserId(userId);
        return toDataRequest(req, AuthReqEntity.class);
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
