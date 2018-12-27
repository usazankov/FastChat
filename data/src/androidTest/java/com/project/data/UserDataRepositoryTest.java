package com.project.data;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.requests.UserListRequest;
import com.project.apifastchat.stores.UserDataStore;
import com.project.data.cache.StoroCacheManager;
import com.project.data.repository.UserDataRepository;
import com.project.data.repository.datasource.UserDataStoreFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@RunWith(AndroidJUnit4.class)
public class UserDataRepositoryTest extends CommonRepository{

    private UserDataRepository userDataRepository;
    private List<User> list;

    @Before
    public void setUp() {
        super.setUp();
        userDataRepository = new UserDataRepository(new UserDataStoreFactory(appContext, new StoroCacheManager(path, 5 * 1024 * 1024 * 1024), networkManager));
    }

    @Test
    public void testGetUsersCase() {
        list = null;
        userDataRepository.getUserList(new UserListRequest())
        .subscribe(new Observer<List<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<User> value) {
                list = value;
            }

            @Override
            public void onError(Throwable e) {
                error = e;
                cont();
            }

            @Override
            public void onComplete() {
                cont();
            }
        });
        blockOnEvent();
        checkOnError();
        Assert.assertNotNull("Пустой список пользователей", list);
        if(list != null)
            Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetUserHappyCase() {
    }
}
