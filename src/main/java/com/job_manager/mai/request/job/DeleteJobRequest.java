package com.job_manager.mai.request.job;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class DeleteJobRequest extends JobRequest {
    private Set<String> ids = new HashSet<>();
}
