package com.job_manager.mai.repository;

import com.job_manager.mai.contrains.JobStatus;
import com.job_manager.mai.model.User;
import com.job_manager.mai.model.UserJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserJobRepository extends JpaRepository<UserJob, Long> {
    List<UserJob> findAllByUser(User user);

    int countAllByUserAndStatusAndUpdatedAtBetween(User user, JobStatus jobStatus, Date timeStart, Date timeEnd);
    List<UserJob> findAllByUserAndUpdatedAtBetween(User user, Date timeStart, Date timeEnd);

    int countAllByUserAndStatus(User user, JobStatus jobStatus);

    List<UserJob> findAllByStatus(JobStatus jobStatus);
}
