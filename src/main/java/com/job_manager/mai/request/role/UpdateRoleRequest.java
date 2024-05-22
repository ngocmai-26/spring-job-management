package com.job_manager.mai.request.role;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UpdateRoleRequest extends RoleRequest {
    private String description;
    private Set<Long> permissionIds = new HashSet<>();

}
