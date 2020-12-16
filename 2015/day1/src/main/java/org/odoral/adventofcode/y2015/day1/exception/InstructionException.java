package org.odoral.adventofcode.y2015.day1.exception;

public class InstructionException extends RuntimeException{

    public InstructionException() {
    }

    public InstructionException(String s) {
        super(s);
    }

    public InstructionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InstructionException(Throwable throwable) {
        super(throwable);
    }

    public InstructionException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
