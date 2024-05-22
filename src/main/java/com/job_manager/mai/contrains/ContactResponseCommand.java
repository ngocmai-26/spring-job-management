package com.job_manager.mai.contrains;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContactResponseCommand {
    ACCEPT(1),
    DENIED(0);

    private final int value;
}
