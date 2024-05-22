package com.job_manager.mai.service.checkin;

import com.job_manager.mai.contrains.TimeKeepShift;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CheckInService {
    public ResponseEntity<?> getScheduleToday(String userId) throws Exception;

    public ResponseEntity<?> checkIn(String userId, TimeKeepShift shift) throws Exception;

    public ResponseEntity<?> checkout(String userId, TimeKeepShift shift) throws Exception;

    public ResponseEntity<?> getAll(Pageable pageable) throws Exception;

    public ResponseEntity<?> getByUser(Pageable pageable, String userId) throws Exception;

    public ResponseEntity<?> getByUserManager(Pageable pageable, String userId) throws Exception;

}
