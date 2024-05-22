package com.job_manager.mai.repository;

import com.job_manager.mai.model.KpiCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KpiCategoryRepository extends JpaRepository<KpiCategory, String> {
    Page<KpiCategory> findAllByNameContaining(Pageable pageable, String name);
}
