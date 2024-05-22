package com.job_manager.mai.model;

import com.job_manager.mai.contrains.ScheduleType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PlanSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int timeStart;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;
}
