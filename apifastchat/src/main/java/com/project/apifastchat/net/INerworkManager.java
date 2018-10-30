package com.project.apifastchat.net;

import com.project.apifastchat.entity.Event;
import com.project.apifastchat.requests.ARequest;

import java.util.List;

import io.reactivex.Observable;


public interface INerworkManager {

    /**
     *  Запрос на сервер
     */
    Observable<String> executeRequest(ARequest request);

    /**
     *  Запросы на сервер
     */
    Observable<String> executeRequest(List<ARequest> request);

    /**
     *  Узнать текущее состояние соединения
     */
    ConnectState getCurrentState();

    /**
     * Установить клиента соединений
     */
    void setCommLink(ICommLink commLink);

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
        void onChangeState(ConnectState stateNew);
    }

    enum ConnectState{
        UNDEFINED,
        CONNECTING,
        ONLINE,
        OFFLINE
    }
}
