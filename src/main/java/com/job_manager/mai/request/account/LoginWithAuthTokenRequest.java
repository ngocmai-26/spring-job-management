package com.job_manager.mai.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginWithAuthTokenRequest{
    @NotBlank
    private String token;
}
