package com.job_manager.mai.request.kpi;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateDetailRequest {
    private String note;

    private String comment;

    private Date timeStart;

    private Date timeEnd;
}
