package com.project.apifastchat.requests;

import com.project.apifastchat.entity.Command;

public abstract class Request {

    public Request(){
    }

    public String toString(){
        return createRequest();
    }
    public abstract String createRequest();
}
