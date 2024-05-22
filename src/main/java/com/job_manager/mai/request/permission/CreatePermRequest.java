package com.job_manager.mai.request.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePermRequest extends PermissionRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @NotBlank(message = "Mô tả không được để trống")
    private String description;

}
