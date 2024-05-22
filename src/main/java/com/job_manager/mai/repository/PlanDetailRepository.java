package com.job_manager.mai.repository;

import com.job_manager.mai.model.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail,String> {
}
