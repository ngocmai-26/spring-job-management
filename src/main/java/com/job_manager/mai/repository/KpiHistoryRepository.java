package com.job_manager.mai.repository;

import com.job_manager.mai.model.KpiHistory;
import com.job_manager.mai.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface KpiHistoryRepository extends JpaRepository<KpiHistory, Long> {
    Page<KpiHistory> findAllByUserAndCreatedAtIsBetween(Pageable pageable, User user, Date start, Date end);
}
