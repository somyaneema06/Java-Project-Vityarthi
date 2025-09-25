package edu.ccrm.exceptions;

/**
 * Custom unchecked exception (runtime) for exceeding credits.
 */
public class MaxCreditLimitExceededException extends RuntimeException {
    public MaxCreditLimitExceededException(String message) {
        super(message);
    }
}
