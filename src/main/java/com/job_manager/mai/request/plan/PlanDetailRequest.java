package com.job_manager.mai.request.plan;

import com.job_manager.mai.contrains.PlanType;
import com.job_manager.mai.contrains.ScheduleType;
import com.job_manager.mai.model.PlanSchedule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PlanDetailRequest {

    @NotBlank(message = "Description not blank")
    private String description;

    @NotNull(message = "Plant Type not null")
    private PlanType planType;
    private String note;

    @NotNull(message = "TimeStart not null")
    private Date timeStart;

    @NotNull(message = "TimeEnd not null")
    private Date timeEnd;

    private List<Integer> planSchedules = new ArrayList<>();
    private ScheduleType scheduleType;
}
