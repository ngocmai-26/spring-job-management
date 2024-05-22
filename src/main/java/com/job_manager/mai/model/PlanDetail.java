package com.job_manager.mai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.job_manager.mai.contrains.PlanType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class PlanDetail {
    @Id
    private String id;

    private String description;
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @ManyToMany
    private List<PlanSchedule> planSchedules = new ArrayList<>();

    private String note;

    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    private Date timeStart;
    private Date timeEnd;

    public void addSchedule(PlanSchedule planSchedule) {
        this.planSchedules.add(planSchedule);
    }

    public void removeSchedule(PlanSchedule planSchedule) {
        this.planSchedules.remove(planSchedule);
    }

    public void clearSchedule() {
        this.planSchedules.clear();
    }

    public PlanDetail() {
        this.id = UUID.randomUUID().toString();
    }
    @JsonIgnore
    public int[] getMaxMinSchedule() {
        int min = planSchedules.get(0).getTimeStart();
        int max = planSchedules.get(0).getTimeStart();
        for (PlanSchedule planSchedule : this.planSchedules) {
            min = Math.min(planSchedule.getTimeStart(), min);
            max = Math.max(planSchedule.getTimeStart(), max);
        }
        return new int[]{min, max};
    }
}
