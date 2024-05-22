package com.job_manager.mai.contrains;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    TYPE_CONTACT(1),
    TYPE_PLAN(4),
    TYPE_JOB(2),
    TYPE_CREATE_ROOM(3);
    private final int value;
}
