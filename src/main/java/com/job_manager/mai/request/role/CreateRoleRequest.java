package com.job_manager.mai.request.role;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class CreateRoleRequest extends RoleRequest {
    @NotBlank(message = "Tên chức vụ không được để trống")
    private String roleName;

    @NotBlank(message = "Mô tả chức vụ không được để trống")
    private String description;

    @Nullable
    private Set<Long> permissionIds;
}
