package com.job_manager.mai.request.account;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String verifyCode;
}
