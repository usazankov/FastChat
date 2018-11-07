package com.project.apifastchat;

import android.os.ConditionVariable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.project.apifastchat.entity.AuthRespEntity;
import com.project.apifastchat.entity.CodeResp;
import com.project.apifastchat.entity.Command;
import com.project.apifastchat.entity.CommonMsg;
import com.project.apifastchat.entity.User;
import com.project.apifastchat.mappers.AuthRespJsonMapper;
import com.project.apifastchat.mappers.CommonJsonMapper;
import com.project.apifastchat.mappers.MessagesJsonMapper;
import com.project.apifastchat.mappers.UsersJsonMapper;
import com.project.apifastchat.net.INetworkManager;
import com.project.apifastchat.net.NetworkManager;
import com.project.apifastchat.net.TcpClient;
import com.project.apifastchat.requests.AuthRequest;
import com.project.apifastchat.requests.MessageRequest;
import com.project.apifastchat.requests.UserListRequest;
import com.project.apifastchat.stores.AuthDataStore;
import com.project.apifastchat.stores.EventDataStore;
import com.project.apifastchat.stores.MessageDataStore;
import com.project.apifastchat.stores.UserDataStore;
import com.project.apifastchat.stores.interfaces.IEventDataStore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class StoresTest {
    private NetworkManager networkManager;
    //Auth
    private AuthRespJsonMapper authMapper;
    private AuthRespEntity authResp;

    //Users
    private UsersJsonMapper usersMapper;
    private List<User> usersResp;

    //Messages
    private MessagesJsonMapper messagesMapper;
    private CommonMsg messageResp;

    private Throwable error;
    private static final String USER_ID = "test_user";
    private static final String USER_NAME = "Yuri";

    protected final ConditionVariable cv = new ConditionVariable();

    private void resume(){
        cv.open();
    }
    private void checkOnError(){
        assertFalse(error == null ? "" : error.getMessage(), error != null);
    }
    private void blockOnEvent(){
        assertFalse("Таймаут приема данных", !cv.block(90000));
    }

    @Before
    public void setUp(){
        networkManager = new NetworkManager(
                new TcpClient(InstrumentationRegistry.getTargetContext()));
        authMapper = new AuthRespJsonMapper();
        usersMapper = new UsersJsonMapper();
        messagesMapper = new MessagesJsonMapper();
    }

    public static AuthRequest createFakeAuthReq(){
        return AuthRequest.newBuilder()
                .setUserId(USER_ID)
                .setUserName(USER_NAME)
                .build();
    }

    @Test
    public void AuthStores_case1(){
        AuthDataStore authDataStore = new AuthDataStore(networkManager, authMapper);
        Observable<AuthRespEntity> auth = authDataStore.doAuthentication(createFakeAuthReq());
        auth.subscribe(new Observer<AuthRespEntity>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AuthRespEntity value) {
                authResp = value;
            }

            @Override
            public void onError(Throwable e) {
                error = e;
                resume();
            }

            @Override
            public void onComplete() {
                resume();
            }
        });
        blockOnEvent();
        checkOnError();
        assertEquals(authResp.getUserId(), USER_ID);
        assertEquals(authResp.getCode_resp(), CodeResp.Success);
    }

    private UserListRequest createFakeReqUserList(){
        return UserListRequest.newBuilder().build();
    }

    @Test
    public void UserDataStore_case1(){
        List<String> testList = new ArrayList<>(3);
        testList.add("user1");
        testList.add("user2");
        testList.add("user3");
        testList.add("Test_user");
        UserDataStore userDataStore = new UserDataStore(networkManager, usersMapper);
        Observable<List<User>> resp = userDataStore.getUsersList(createFakeReqUserList());
        resp.subscribe(new Observer<List<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<User> value) {
                usersResp = value;
            }

            @Override
            public void onError(Throwable e) {
                error = e;
                resume();
            }

            @Override
            public void onComplete() {
                resume();
            }
        });
        blockOnEvent();
        checkOnError();
        Assert.assertNotNull("Пустой список пользователей", usersResp);

        for (User i : usersResp) {
            assertFalse("Нет пользователя " + i.getId(), !testList.contains(i.getId()));
        }
    }

    private MessageRequest createMessageRequest(){
        return MessageRequest.newBuilder()
                .setUserIdFrom("test_user")
                .setMessageBody("Hello world!")
                .setTimeMessage("18:31")
                .setDateMessage("24.10.2018")
                .build();
    }

    @Test
    public void MessageDataStore_case1(){
        MessageDataStore messageDataStore = new MessageDataStore(networkManager, messagesMapper);
        messageDataStore.sendMsg(createMessageRequest())
                .subscribe(new Observer<CommonMsg>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonMsg value) {
                        messageResp = value;
                    }

                    @Override
                    public void onError(Throwable e) {
                        error = e;
                        resume();
                    }

                    @Override
                    public void onComplete() {
                        resume();
                    }
                });
        blockOnEvent();
        checkOnError();
        Assert.assertNotNull("Не получен ответ от сервера", messageResp);
        Assert.assertEquals(messageResp.getCode_resp(), CodeResp.Success);
        Assert.assertEquals(messageResp.getCommand_obj().getCommandId(), Command.CommandId.SendMsg);
    }


    public void EventDataStore_case1(){
        IEventDataStore eventDataStore = new EventDataStore(networkManager, new CommonJsonMapper());
        eventDataStore.getConnectState()
                .subscribe(new Observer<INetworkManager.ConnectState>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(INetworkManager.ConnectState value) {
                        Log.d("ConnectState", value.name());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        blockOnEvent();
    }
}
