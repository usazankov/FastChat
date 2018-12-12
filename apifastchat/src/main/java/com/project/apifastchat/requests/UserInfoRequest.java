package com.project.apifastchat.requests;

import com.project.apifastchat.entity.Command;
import com.project.apifastchat.entity.CommonMsg;

public class UserInfoRequest extends ARequest{

    private String userInfoId;

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }
    @Override
    public String createRequest() {
        CommonMsg req = new CommonMsg();
        Command com = new Command();
        com.setCommandId(Command.CommandId.GetUserInfo);
        req.setCommand_obj(com);
        return toDataRequest(req, CommonMsg.class);
    }

    public static UserInfoRequest.Builder newBuilder() {
        return new UserInfoRequest().new Builder();
    }

    public class Builder{
        private Builder() {
            // private constructor
        }

        public UserInfoRequest build(){
            return UserInfoRequest.this;
        }
    }
}
