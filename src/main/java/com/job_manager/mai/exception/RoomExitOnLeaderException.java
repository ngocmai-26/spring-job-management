package com.job_manager.mai.exception;

import lombok.AllArgsConstructor;

public class RoomExitOnLeaderException extends Exception {
    public RoomExitOnLeaderException(String msg) {
        super(msg);
    }
}
