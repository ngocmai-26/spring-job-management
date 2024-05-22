package com.job_manager.mai.repository;

import com.job_manager.mai.contrains.WorkDay;
import com.job_manager.mai.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByWorkDay(WorkDay workday);
}
