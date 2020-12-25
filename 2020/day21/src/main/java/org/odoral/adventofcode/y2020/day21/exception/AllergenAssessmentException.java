package org.odoral.adventofcode.y2020.day21.exception;

public class AllergenAssessmentException extends RuntimeException {
    public AllergenAssessmentException() {
    }

    public AllergenAssessmentException(String s) {
        super(s);
    }

    public AllergenAssessmentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AllergenAssessmentException(Throwable throwable) {
        super(throwable);
    }

    public AllergenAssessmentException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
