package com.job_manager.mai.repository;

import com.job_manager.mai.contrains.JobStatus;
import com.job_manager.mai.model.Job;
import com.job_manager.mai.model.User;
import com.job_manager.mai.model.UserJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    List<Job> findAllByUserJobsIn(Pageable pageable, List<UserJob> userJobs);
    Page<Job> findAllByDisplay(Pageable pageable, boolean display);

    // find all job not done yet
    List<Job> findAllByDisplay(boolean display);
    int countAllByManager(User manager);
    List<Job> findAllByManagerAndUpdatedAtBetween(User manager, Date timeStart, Date timeEnd);
}
