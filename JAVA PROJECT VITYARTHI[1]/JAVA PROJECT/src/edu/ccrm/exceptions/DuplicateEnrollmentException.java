package edu.ccrm.exceptions;

/**
 * Custom checked exception for duplicate enrollment.
 */
public class DuplicateEnrollmentException extends Exception {
    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}
