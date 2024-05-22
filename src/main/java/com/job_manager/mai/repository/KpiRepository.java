package com.job_manager.mai.repository;

import com.job_manager.mai.model.Kpi;
import com.job_manager.mai.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KpiRepository extends JpaRepository<Kpi,String> {
    Page<Kpi> findAllByNameContaining(Pageable pageable, String name);
    Page<Kpi> findAllByUserContaining(Pageable pageable, User user);
}
