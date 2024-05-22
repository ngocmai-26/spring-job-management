package com.job_manager.mai.model;

import com.job_manager.mai.contrains.JobEvaluate;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class JobDetail {

    @Id
    private String id;

    private String description;

    private String note;

    private String target;

    private Date timeStart;
    private Date timeEnd;
    private String additionInfo;

    public JobDetail() {
        this.id = UUID.randomUUID().toString();
    }
}
