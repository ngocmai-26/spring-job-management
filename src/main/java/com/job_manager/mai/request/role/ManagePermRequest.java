package com.job_manager.mai.request.role;

import lombok.Data;

import java.util.Set;

@Data
public class ManagePermRequest {

    private Set<Long> ids;
}
