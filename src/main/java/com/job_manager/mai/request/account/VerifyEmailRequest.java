package com.job_manager.mai.request.account;

import lombok.Data;

@Data
public class VerifyEmailRequest {
    private String email;
    private String code;
}
