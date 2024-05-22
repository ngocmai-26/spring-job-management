package com.job_manager.mai.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountRequest {

    @NotBlank(message = "Email is required")
    public String username;

    @NotBlank(message = "Password is required")
    public String password;
}
