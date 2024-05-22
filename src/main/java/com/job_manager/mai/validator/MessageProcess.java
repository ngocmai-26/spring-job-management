package com.job_manager.mai.validator;

public class MessageProcess {
    public static final String getMessage(String msg, String... params) {
        return String.format(msg, params);
    }
}
