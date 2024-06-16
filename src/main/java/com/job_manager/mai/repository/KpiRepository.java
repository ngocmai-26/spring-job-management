package com.job_manager.mai.repository;

import com.job_manager.mai.model.Kpi;
import com.job_manager.mai.model.User;
import com.job_manager.mai.model.UserJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface KpiRepository extends JpaRepository<Kpi,String> {
    Page<Kpi> findAllByNameContaining(Pageable pageable, String name);
    Page<Kpi> findAllByUserContaining(Pageable pageable, User user);
    List<Kpi> findAllByUserAndUpdatedAtBetween(User user, Date timeStart, Date timeEnd);
}
