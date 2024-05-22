package com.job_manager.mai.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class KpiDetail {
    @Id
    private String id;

    private String note;
    private String comment;

    private Date timeStart;

    private Date timeEnd;

    public KpiDetail() {
        this.id = UUID.randomUUID().toString();
    }

}
