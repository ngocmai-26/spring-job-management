package com.job_manager.mai.repository;

import com.job_manager.mai.model.PlanSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanScheduleRepository extends JpaRepository<PlanSchedule, Long> {
}
