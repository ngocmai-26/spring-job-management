package com.job_manager.mai.response.account;

import com.job_manager.mai.model.Role;
import com.job_manager.mai.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class AccountResponse {
    private User user;
    private Role role;
    private String token;

    private boolean isVerify;
    private boolean isActive;
    private Date lastTimeLogin;
    private String lastIpLogin;

}
