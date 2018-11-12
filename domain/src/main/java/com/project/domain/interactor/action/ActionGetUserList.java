package com.project.domain.interactor.action;

import com.project.apifastchat.entity.User;
import com.project.apifastchat.requests.UserListRequest;
import com.project.domain.executor.PostExecutionThread;
import com.project.domain.executor.ThreadExecutor;

import java.util.List;

import io.reactivex.Observable;

public class ActionGetUserList extends AUseCase<List<User>, UserListRequest> {
    public ActionGetUserList(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread){
        super(threadExecutor, postExecutionThread);
    }

    @Override
    Observable<List<User>> buildUseCaseObservable(UserListRequest userListRequest) {
        return null;
    }
}
