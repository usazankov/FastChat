package com.project.apifastchat.exceptions;

import java.io.IOException;

public class NetworkException extends Exception {
    public NetworkException() {
        super();
    }

    public NetworkException(final Throwable cause) {
        super(cause);
    }
}
