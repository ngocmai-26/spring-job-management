package com.job_manager.mai.contrains;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    NORMAL_MESSAGE(1), NOTIFICATION_MESSAGE(2);
    private final int val;
}
