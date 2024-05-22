package com.job_manager.mai.service.role;

import com.job_manager.mai.request.role.*;
import com.job_manager.mai.service.inteface.IBaseService;
import org.springframework.http.ResponseEntity;

public interface RoleService extends IBaseService<RoleRequest, CreateRoleRequest, UpdateRoleRequest, DeleteRoleRequest, Long> {
    ResponseEntity<?> addPerm(long id, ManagePermRequest request) throws Exception;

    ResponseEntity<?> removePerm(long id, ManagePermRequest request) throws Exception;

}
