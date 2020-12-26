package org.odoral.adventofcode.y2020.day23.exception;

public class CrabCupsException extends RuntimeException {
    public CrabCupsException() {
    }

    public CrabCupsException(String message) {
        super(message);
    }

    public CrabCupsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrabCupsException(Throwable cause) {
        super(cause);
    }

    public CrabCupsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
