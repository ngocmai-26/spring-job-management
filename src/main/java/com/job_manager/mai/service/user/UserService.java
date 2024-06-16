package com.job_manager.mai.service.user;

import com.job_manager.mai.request.user.*;
import com.job_manager.mai.service.inteface.IBaseService;
import org.springframework.http.ResponseEntity;

public interface UserService extends IBaseService<UserRequest, CreateUserRequest, UpdateUserRequest, DeleteUserRequest, String> {

    ResponseEntity<?> addStaff(StaffChangeRequest request) throws Exception;

    ResponseEntity<?> removeStaff(StaffChangeRequest request) throws Exception;
}
