package com.project.domain.interactor.actions;

import com.project.domain.executor.PostExecutionThread;
import com.project.domain.executor.ThreadExecutor;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class AUseCase<T, Params> {
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private final CompositeDisposable disposables;

    AUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.disposables = new CompositeDisposable();
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link AUseCase}.
     */
    abstract Observable<T> buildUseCaseObservable(Params params);

    /**
     * Executes the current use case.
     *
     * @param observer {@link DisposableObserver} which will be listening to the observable build
     * by {@link #buildUseCaseObservable(Params)} ()} method.
     * @param params Parameters (Optional) used to build/execute this use case.
     */
    public Disposable execute(DisposableObserver<T> observer, Params params) {

        //Preconditions.checkNotNull(observer);
        final Observable<T> observable = this.buildUseCaseObservable(params)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler());
        Disposable disposable = observable.subscribeWith(observer);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void disposeAll() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    public void dispose(Disposable disposable){
        disposable.dispose();
        disposables.remove(disposable);
    }
    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    private void addDisposable(Disposable disposable) {
        //Preconditions.checkNotNull(disposable);
        //Preconditions.checkNotNull(disposables);
        disposables.add(disposable);
    }
}
