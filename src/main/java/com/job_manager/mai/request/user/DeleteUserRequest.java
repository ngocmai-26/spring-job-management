package com.job_manager.mai.request.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeleteUserRequest extends UserRequest {
    public List<String> ids = new ArrayList<>();
}
