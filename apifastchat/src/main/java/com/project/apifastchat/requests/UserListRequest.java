package com.project.apifastchat.requests;

import com.project.apifastchat.entity.AuthReqEntity;
import com.project.apifastchat.entity.Command;
import com.project.apifastchat.entity.CommonMsg;

public class UserListRequest extends ARequest {

    @Override
    public String createRequest() {
        CommonMsg req = new CommonMsg();
        Command com = new Command();
        com.setCommandId(Command.CommandId.GetUsers);
        req.setCommand_obj(com);
        return toDataRequest(req, CommonMsg.class);
    }

    public static UserListRequest.Builder newBuilder() {
        return new UserListRequest().new Builder();
    }

    public class Builder{
        private Builder() {
            // private constructor
        }

        public UserListRequest build(){
            return UserListRequest.this;
        }
    }
}
