package com.job_manager.mai.model;

import lombok.Data;

import java.util.concurrent.ScheduledFuture;

@Data
public class PlanTask {
    private String Id;
    private Runnable task;
    private Plan plan;
    private String userId;
    private ScheduledFuture<?> scheduledFuture;

    public PlanTask(Plan plan, Runnable runnable) {
        this.plan = plan;
        this.Id = plan.getId();
        this.task = runnable;
    }
}
