package com.job_manager.mai.request.contact;

import lombok.Data;

@Data
public class ResponseContactRequest {
    private long contactId;
    private int command;
}
