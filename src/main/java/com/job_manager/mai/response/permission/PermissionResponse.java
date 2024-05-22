package com.job_manager.mai.response.permission;

import com.job_manager.mai.model.Permission;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Data
@Component
public class PermissionResponse extends Permission implements IPermissionResponser {
    @Override
    public Collection<PermissionResponse> mapTo(Collection<Permission> source) {
        return null;
    }

    @Override
    public PermissionResponse mapTo(Permission source) {
        PermissionResponse permissionResponse = mapper.map(source, PermissionResponse.class);
        if (permissionResponse.isActive()) {
            return permissionResponse;
        }
        return null;
    }

    @Override
    public Page<PermissionResponse> mapTo(Page<Permission> source) {
        return source.map(this::mapTo);
    }
}
