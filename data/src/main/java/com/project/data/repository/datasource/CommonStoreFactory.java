package com.project.data.repository.datasource;

import android.content.Context;

import com.project.apifastchat.net.INetworkManager;
import com.project.data.cache.ICacheManager;

public class CommonStoreFactory {
    protected final Context context;
    protected final ICacheManager cacheManager;
    protected INetworkManager networkManager;

    public CommonStoreFactory(Context context, ICacheManager cacheManager, INetworkManager networkManager){
        this.context = context;
        this.cacheManager = cacheManager;
        this.networkManager = networkManager;
    }
}
