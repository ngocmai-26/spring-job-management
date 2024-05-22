package com.job_manager.mai.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Date;

@Data
public class RegisterRequest extends AccountRequest {
    @NotBlank(message = "Confirm password required")
    private String confirmPassword;
}
