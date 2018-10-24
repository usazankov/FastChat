package com.project.apifastchat.requests;

import com.project.apifastchat.entity.Command;
import com.project.apifastchat.entity.Message;
import com.project.apifastchat.entity.MessageReqEntity;

public class MessageRequest extends ARequest {
    private String userIdFrom;
    private String messageBody;
    private String dateMsg;
    private String timeMsg;
    private MessageRequest(){
    }
    
    public String createRequest() {
        MessageReqEntity req = new MessageReqEntity();
        Command com = new Command();
        com.setCommandId(Command.CommandId.SendMsg);
        req.setCommand_obj(com);
        req.setUserId(userIdFrom);
        Message mes = new Message();
        mes.setBody(messageBody);
        mes.setDate(dateMsg);
        mes.setTime(timeMsg);
        req.setMessage_obj(mes);
        return toDataRequest(req, MessageReqEntity.class);
    }

    public static MessageRequest.Builder newBuilder() {
        return new MessageRequest().new Builder();
    }

    public class Builder{
        private Builder() {
            // private constructor
        }
        public MessageRequest.Builder setUserIdFrom(String userId){
            MessageRequest.this.userIdFrom = userId;
            return this;
        }
        public MessageRequest.Builder setMessageBody(String msg){
            MessageRequest.this.messageBody = msg;
            return this;
        }

        public MessageRequest.Builder setDateMessage(String value){
            MessageRequest.this.dateMsg = value;
            return this;
        }

        public MessageRequest.Builder setTimeMessage(String value){
            MessageRequest.this.timeMsg = value;
            return this;
        }

        public MessageRequest build(){
            return MessageRequest.this;
        }
    }
}
