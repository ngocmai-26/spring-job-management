package com.job_manager.mai.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class CreateUserRequest extends UserRequest {
    @NotBlank(message = "Firstname is required")
    private String firstName;

    private String avatar;
    @NotBlank(message = "Lastname is required")
    private String lastName;

    @NotBlank(message = "Address is required")
    private String address;
    @NotNull(message = "Birthday is required")
    private Date birthday;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 10)
    private String phone;
}
