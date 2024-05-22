package com.job_manager.mai.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class Kpi {
    @Id
    private String id;

    private boolean verify;
    private String name;

    private String description;

    private int target;

    @UpdateTimestamp
    private Date updatedAt;
    @CreationTimestamp
    private Date createdAt;

    @ManyToOne
    private KpiCategory kpiType;

    @ManyToOne
    private User user;
    @OneToOne
    private KpiDetail detail;

    public Kpi() {
        this.id = UUID.randomUUID().toString();
    }

}
