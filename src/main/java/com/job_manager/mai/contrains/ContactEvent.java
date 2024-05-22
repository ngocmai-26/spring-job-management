package com.job_manager.mai.contrains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContactEvent {
    RESPONSE_REQUEST(1),
    NEW_REQUEST(2);
    private final int value;
}
