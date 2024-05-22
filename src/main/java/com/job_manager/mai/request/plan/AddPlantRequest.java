package com.job_manager.mai.request.plan;

import com.job_manager.mai.contrains.PlanStatus;
import com.job_manager.mai.model.Job;
import com.job_manager.mai.model.PlanSchedule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddPlantRequest extends PlanRequest {
    @NotBlank(message = "title is required")
    private String title;

    private PlanStatus planStatus;

    private List<String> planJob = new ArrayList<>();

    @NotNull
    private PlanDetailRequest planDetailRequest;

}
