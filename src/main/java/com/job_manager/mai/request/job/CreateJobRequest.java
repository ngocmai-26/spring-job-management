package com.job_manager.mai.request.job;

import com.job_manager.mai.contrains.JobStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CreateJobRequest extends JobRequest {
    @NotBlank(message = "Title required")
    private String title;
    @NotNull(message = "Priority required")
    private int priority;

    private int progress = 0;
    @NotNull
    private JobStatus jobStatus = JobStatus.PENDING;

    @NotNull(message = "Point per job is required")
    private int pointPerJob;

    @NotNull
    private boolean isTask;

    @NotBlank(message = "Creator id required")
    private String userCreateJobId;

    private List<String> staffsGotJobId = new ArrayList<>();

    @NotBlank(message = "Description is required")
    private String description;

    private String note;

    private String target;

    private Date timeStart;
    private Date timeEnd;

    private String additionInfo;
}
