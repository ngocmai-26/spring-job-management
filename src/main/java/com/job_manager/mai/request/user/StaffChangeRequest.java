package com.job_manager.mai.request.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class StaffChangeRequest {
    private List<String> staffIds = new ArrayList<>();
    private String managerId;
}
