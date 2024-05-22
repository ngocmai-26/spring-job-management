package com.job_manager.mai.request.user;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateUserRequest extends UserRequest {
    private String firstName;

    private String avatar;
    private String lastName;

    private String address;
    private Date birthday;

    private String department;

    @Size(min = 10, max = 10)
    private String phone;
}
