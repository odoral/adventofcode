package org.odoral.adventofcode.y2020.day22.exception;

public class CrabCombatException extends RuntimeException {
    public CrabCombatException() {
    }

    public CrabCombatException(String message) {
        super(message);
    }

    public CrabCombatException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrabCombatException(Throwable cause) {
        super(cause);
    }

    public CrabCombatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
