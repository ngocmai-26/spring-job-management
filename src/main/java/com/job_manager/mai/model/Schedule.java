package com.job_manager.mai.model;

import com.job_manager.mai.contrains.ScheduleType;
import com.job_manager.mai.contrains.TimeKeepShift;
import com.job_manager.mai.contrains.WorkDay;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
public class Schedule {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    @Enumerated(EnumType.STRING)
    private WorkDay workDay;

    @Enumerated(EnumType.STRING)
    private TimeKeepShift shift;

    private LocalTime timeStart;
    private LocalTime timeEnd;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
