package com.job_manager.mai.repository;

import com.job_manager.mai.contrains.WorkDay;
import com.job_manager.mai.model.TimeKeeping;
import com.job_manager.mai.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CheckInRepository extends JpaRepository<TimeKeeping, Long> {
    @Query(value = "select tk from TimeKeeping tk where tk.userChecked.Id = :userId and tk.checkInTime between :start and :end")
    List<TimeKeeping> findByUserCheckedAndCheckInTimeBetween(String userId, String start, String end);

    List<TimeKeeping> findAllByWorkdayAndUpdatedAtBetweenAndUserChecked(WorkDay workDay, LocalDateTime start, LocalDateTime end, User user);

    List<TimeKeeping> findAllByUserChecked(Pageable pageable, User user);

    List<TimeKeeping> findAllByUserCheckedIn(Pageable pageable, Set<User> users);

    int countAllByUserCheckedAndCheckInTimeBetween(User user, LocalDateTime timeStart, LocalDateTime timeEnd);
}
