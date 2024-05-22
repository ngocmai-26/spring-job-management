package com.job_manager.mai.service.user;

import com.job_manager.mai.request.user.CreateUserRequest;
import com.job_manager.mai.request.user.DeleteUserRequest;
import com.job_manager.mai.request.user.UpdateUserRequest;
import com.job_manager.mai.request.user.UserRequest;
import com.job_manager.mai.service.inteface.IBaseService;

public interface UserService extends IBaseService<UserRequest, CreateUserRequest, UpdateUserRequest, DeleteUserRequest, String> {
}
