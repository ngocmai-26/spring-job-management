package com.job_manager.mai.contrains;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageStatus {

    DISPLAY(1),
    HIDE(2),
    REMOVE(3);
    private final int val;
}
