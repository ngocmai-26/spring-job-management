package com.job_manager.mai.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class KpiCategory {
    @jakarta.persistence.Id
    private String Id;
    private String name;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public KpiCategory() {
        this.Id = UUID.randomUUID().toString();
    }
}
