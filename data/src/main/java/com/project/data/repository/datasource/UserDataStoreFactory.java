package com.project.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.project.apifastchat.mappers.UsersJsonMapper;
import com.project.apifastchat.net.INetworkManager;
import com.project.apifastchat.stores.UserDataStore;
import com.project.apifastchat.stores.interfaces.IUserDataStore;
import com.project.data.cache.ICacheManager;

public class UserDataStoreFactory extends CommonStoreFactory{

    public UserDataStoreFactory(@NonNull Context context, @NonNull ICacheManager cacheManager, INetworkManager networkManager) {
        super(context, cacheManager, networkManager);
    }

    /**
     * Create {@link IUserDataStore} from a user id.
     */
    public IUserDataStore create(final String userId) {
        IUserDataStore userDataStore;

        if (!cacheManager.isExpired(userId) && cacheManager.contains(userId)) {
            userDataStore = new DiskUserDataStore(cacheManager);
        } else {
            userDataStore = createCloudDataStore();
        }

        return userDataStore;
    }

    /**
     * Create {@link IUserDataStore} to retrieve data from the Cloud.
     */
    public IUserDataStore createCloudDataStore() {
        final UsersJsonMapper userEntityJsonMapper = new UsersJsonMapper();
        return new UserDataStore(networkManager, userEntityJsonMapper);
    }
}
