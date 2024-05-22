package com.job_manager.mai.repository;

import com.job_manager.mai.model.KpiDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KpiRepositoryDetail extends JpaRepository<KpiDetail, String> {
}
