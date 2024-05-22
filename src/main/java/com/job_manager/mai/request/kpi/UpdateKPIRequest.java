package com.job_manager.mai.request.kpi;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateKPIRequest extends KPIRequest{
    private String name;
    private String description;
    private int target;
    private String kpiTypeId;
}
