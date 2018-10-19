package com.project.apifastchat;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.io.IOException;

public interface ICommLink {

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground
    public interface ICommLinkListener {
        public void messageReceived(String message);
        public void onError(Throwable e);
    }

    /**
     * Возвращает true, если соединение установлено (или устанавливается в данный момент)
     * Если не установлено, то false.  Другими словами, если true, то connect() вызывать
     * не требуется.
     *
     * @return false, если не установлено, иначе - true.
     */
    boolean isConnected();

    /**
     * Устанавливает соединение, если оно не установлено.
     *
     * @return true - если потребовалось установить соединеие,
     *                 и установка прошла успешно.
     *         false - если не потребовалось установить соединение
     *                 (т.е. уже установлено)
     * @throws IOException - если потребовалось установить соединеие,
     *                 и установка прошла НЕ успешно.
     */
    void run() throws IOException;

    /**
     * Прекращает слушать сокет соединение.
     */
    void stop();

    /**
     * Опустошает заполненый до конца буфер записи вышестоящего потока.
     * Операция блокируется, если требуется нижестоящими уровнями.
     * В случае, если хотя бы часть буфера не может быть опустошена,
     * будет вызвано исключение CommException, после которого следует
     * считать канал передачи данных невалидным (следует разорвать
     * соединение {@link #stop()}).
     *
     * @param str - буфер записи вышестоящего потока.
     * @throws IOException
     */
    void send(@NonNull String str) throws IOException;

    /**
     * Устанавливает таймаут соединения (смотри {@link #run()} )
     * @param msecs - Количество миллисекунд, через которые будет
     *              выброшено исключение IOException при отсуствии соединения.
     */
    void setConnectionTimeout(int msecs);

    /**
     * По умолчанию определяется реализацией (обычно 20 000 миллисекунд).
     *
     * @param msecs - Количество миллисекунд, через которые будет
     *              выброшено исключение CommException в случае, если
     *              данные не получены от источника.
     */

    void setRecvTimeoutMsecs(int msecs);

}
