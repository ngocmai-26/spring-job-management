package com.job_manager.mai.request.plan;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeletePlanRequest extends PlanRequest {

    private List<String> ids = new ArrayList<>();
}
