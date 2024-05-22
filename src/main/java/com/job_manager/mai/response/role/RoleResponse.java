package com.job_manager.mai.response.role;

import com.job_manager.mai.model.Role;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@Component
public class RoleResponse extends Role implements IRoleResponser {

    @Override
    public Collection<RoleResponse> mapTo(Collection<Role> source) {
        Collection<RoleResponse> responses = source.stream().map(this::mapTo).collect(Collectors.toSet());
        // TODO : do something other here
        return responses;
    }

    @Override
    public RoleResponse mapTo(Role source) {
        RoleResponse roleResponse = mapper.map(source, RoleResponse.class);
        // TODO : do something other here
        return roleResponse;
    }

    @Override
    public Page<RoleResponse> mapTo(Page<Role> source) {
        return source.map(this::mapTo);
    }
}
