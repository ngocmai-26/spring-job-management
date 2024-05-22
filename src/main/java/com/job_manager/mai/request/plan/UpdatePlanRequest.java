package com.job_manager.mai.request.plan;

import com.job_manager.mai.contrains.PlanStatus;
import com.job_manager.mai.model.Job;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdatePlanRequest extends PlanRequest {
    private String title;
    private PlanStatus planStatus;

    private List<String> planJob = new ArrayList<>();
    private PlanDetailRequest planDetailRequest;
}
