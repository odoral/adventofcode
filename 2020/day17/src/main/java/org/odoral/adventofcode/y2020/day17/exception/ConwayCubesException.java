package org.odoral.adventofcode.y2020.day17.exception;

public class ConwayCubesException extends RuntimeException {
    public ConwayCubesException() {
    }

    public ConwayCubesException(String message) {
        super(message);
    }

    public ConwayCubesException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConwayCubesException(Throwable cause) {
        super(cause);
    }

    public ConwayCubesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
