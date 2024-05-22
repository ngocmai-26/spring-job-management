package com.job_manager.mai.service.permission;

import com.job_manager.mai.request.permission.CreatePermRequest;
import com.job_manager.mai.request.permission.DeletePermRequest;
import com.job_manager.mai.request.permission.PermissionRequest;
import com.job_manager.mai.request.permission.UpdatePermRequest;
import com.job_manager.mai.service.inteface.IBaseService;

public interface PermissionService extends IBaseService<PermissionRequest, CreatePermRequest, UpdatePermRequest, DeletePermRequest, Long> {
}
