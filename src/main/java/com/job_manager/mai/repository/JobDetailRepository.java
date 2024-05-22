package com.job_manager.mai.repository;

import com.job_manager.mai.model.JobDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailRepository extends JpaRepository<JobDetail,String> {
}
