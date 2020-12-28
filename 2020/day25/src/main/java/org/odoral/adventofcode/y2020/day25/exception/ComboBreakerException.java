package org.odoral.adventofcode.y2020.day25.exception;

public class ComboBreakerException extends RuntimeException {
    public ComboBreakerException() {
    }

    public ComboBreakerException(String message) {
        super(message);
    }

    public ComboBreakerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComboBreakerException(Throwable cause) {
        super(cause);
    }

    public ComboBreakerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
