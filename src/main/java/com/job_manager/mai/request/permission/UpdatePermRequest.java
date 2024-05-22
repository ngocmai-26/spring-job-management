package com.job_manager.mai.request.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePermRequest extends PermissionRequest {

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

}
