package com.job_manager.mai.request.role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class DeleteRoleRequest extends RoleRequest {
    @NotNull(message = "Role ids không được để trống")
    Set<Long> roleIds;
}
