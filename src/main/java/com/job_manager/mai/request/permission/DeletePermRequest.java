package com.job_manager.mai.request.permission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class DeletePermRequest extends PermissionRequest {
    @NotNull
    private Set<Long> deleteIds = new HashSet<>();
}
