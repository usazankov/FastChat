package com.project.apifastchat.stores;

import com.project.apifastchat.net.INetworkManager;

public class CommonDataStore {

    protected INetworkManager networkManager;
    public CommonDataStore(INetworkManager networkManager){
        this.networkManager = networkManager;
    }


}
