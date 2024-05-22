package com.job_manager.mai.exception;

public class PasswordConfirmIncorrectException extends Exception {
    public PasswordConfirmIncorrectException(String passwordConfirmIncorrect) {
        super(passwordConfirmIncorrect);
    }
}
