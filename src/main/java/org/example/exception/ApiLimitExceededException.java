package org.example.exception;


public class ApiLimitExceededException extends Exception {
    public ApiLimitExceededException(String message) {
        super(message);
    }
}
