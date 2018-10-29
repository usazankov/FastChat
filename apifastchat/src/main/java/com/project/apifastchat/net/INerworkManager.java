package com.project.apifastchat.net;

import com.project.apifastchat.entity.Event;
import com.project.apifastchat.requests.ARequest;

import io.reactivex.Observable;


public interface INerworkManager {

    /**
     *  Запрос на сервер
     */
    Observable<String> executeRequest(ARequest request);

    /**
     *  Есть ли соединение с сервером
     */
    boolean isConnected();

    /**
     *  Запустить
     */
    void start();

    /**
     * Установить слушателя событий от сервера
     */
    void setEventListener(IEventListener eventListener);

    /**
     * Установить слушателя состояния соединения
     */
    void setConnectStateListener(IConnectStateListener connectStateListener);
    /**
     * Интерфейс слушателя событий от сервера
     */
    interface IEventListener{
        void onEvent(Event.EventId eventId, String data);
    }

    /**
     * Интерфейс слушателя состояния соединения
     */
    interface IConnectStateListener{
        void onChangeState(ConnectState stateNew, ConnectState stateOld);
    }

    public enum ConnectState{
        UNDEFINED,
        CONNECTING,
        ONLINE,
        OFFLINE
    }
}
