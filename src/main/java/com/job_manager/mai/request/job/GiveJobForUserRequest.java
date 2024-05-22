package com.job_manager.mai.request.job;

import lombok.Data;

import java.util.List;

@Data
public class GiveJobForUserRequest {
    private List<String> userIds;
}
