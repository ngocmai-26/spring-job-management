package com.job_manager.mai.request.job;

import com.job_manager.mai.contrains.JobEvaluate;
import lombok.Data;

import java.util.Date;

@Data
public class JobDetailUpdateRequest {
    private String description;
    private String note;
    private String target;

    private Date timeStart;
    private Date timeEnd;
    private String additionInfo;
}
