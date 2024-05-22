package com.job_manager.mai.model;

import com.job_manager.mai.contrains.TimeKeepShift;
import com.job_manager.mai.contrains.TimeKeepingStatus;
import com.job_manager.mai.contrains.WorkDay;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class TimeKeeping {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private LocalDateTime checkInTime = LocalDateTime.now();
    private LocalDateTime checkoutTime = LocalDateTime.now();
    private boolean isCheckedOutToDay = false;
    @Enumerated(EnumType.STRING)
    private TimeKeepingStatus status;
    private long overTime;
    private long late;

    private TimeKeepShift shift;

    @Enumerated(EnumType.STRING)
    private WorkDay workday;

    @ManyToOne
    private User userChecked;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
