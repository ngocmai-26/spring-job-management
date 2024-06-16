package com.job_manager.mai.repository;

import com.job_manager.mai.contrains.PlanStatus;
import com.job_manager.mai.model.Kpi;
import com.job_manager.mai.model.Plan;
import com.job_manager.mai.model.User;
import com.job_manager.mai.model.UserJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plan, String> {
    Page<Plan> findAllByTitleContaining(Pageable pageable, String query);

    int countAllByStatus(PlanStatus planStatus);
    List<Plan> findAllByStatus(PlanStatus planStatus);

}
