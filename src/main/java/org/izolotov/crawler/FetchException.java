package org.izolotov.crawler;

public class FetchException extends Exception {

    public FetchException() {
        super();
    }

    public FetchException(String message) {
        super(message);
    }

    public FetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public FetchException(Throwable cause) {
        super(cause);
    }

}
