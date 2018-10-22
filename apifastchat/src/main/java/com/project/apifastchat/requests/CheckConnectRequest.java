package com.project.apifastchat.requests;

import com.project.apifastchat.entity.CheckConnectEntity;
import com.project.apifastchat.entity.Command;

public class CheckConnectRequest extends ARequest {
    @Override
    public String createRequest() {
        CheckConnectEntity req = new CheckConnectEntity();
        Command com = new Command();
        com.setCommandId(Command.CommandId.CheckConnect);
        req.setCommand_obj(com);
        return toDataRequest(req, CheckConnectEntity.class);
    }
}
