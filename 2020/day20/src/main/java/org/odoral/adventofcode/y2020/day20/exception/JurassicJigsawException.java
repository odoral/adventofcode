package org.odoral.adventofcode.y2020.day20.exception;

public class JurassicJigsawException extends RuntimeException {
    public JurassicJigsawException() {
    }

    public JurassicJigsawException(String s) {
        super(s);
    }

    public JurassicJigsawException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public JurassicJigsawException(Throwable throwable) {
        super(throwable);
    }

    public JurassicJigsawException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
