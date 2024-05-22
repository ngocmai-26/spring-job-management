package com.job_manager.mai.service.checkin;

import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.contrains.TimeKeepShift;
import com.job_manager.mai.contrains.TimeKeepingStatus;
import com.job_manager.mai.contrains.WorkDay;
import com.job_manager.mai.exception.UserNotFoundException;
import com.job_manager.mai.model.Schedule;
import com.job_manager.mai.model.TimeKeeping;
import com.job_manager.mai.model.User;
import com.job_manager.mai.repository.CheckInRepository;
import com.job_manager.mai.repository.ScheduleRepository;
import com.job_manager.mai.repository.UserRepository;
import com.job_manager.mai.service.kpi.KpiHistoryService;
import com.job_manager.mai.util.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CheckInServiceImpl implements CheckInService {
    private final CheckInRepository checkInRepository;

    private final KpiHistoryService kpiHistoryService;
    private final UserRepository userRepository;

    private final ScheduleRepository scheduleRepository;

    private final float TOTAL_NEED_CHECKIN_TIME = 44f;
    private final float TOTAL_CHECKIN_SCORE = 5f;

    private float SCORE_PER_TIME = TOTAL_CHECKIN_SCORE / TOTAL_NEED_CHECKIN_TIME;

    public ResponseEntity<?> getScheduleToday(String userId) throws Exception {
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        WorkDay workDay = mapToWorkDay(dayOfWeek);
        LocalDateTime timeStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime timeEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        User userCheck = userRepository.findById(userId).orElse(null);
        List<TimeKeeping> timeKeepings = checkInRepository.findAllByWorkdayAndUpdatedAtBetweenAndUserChecked(workDay, timeStart, timeEnd, userCheck);
        return ApiResponseHelper.success(timeKeepings);
    }

    @Override
    public ResponseEntity<?> checkIn(String userId, TimeKeepShift shift) throws Exception {
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        WorkDay workDay = mapToWorkDay(dayOfWeek);
        List<Schedule> schedules = scheduleRepository.findAllByWorkDay(workDay).stream().filter(s -> s.getShift().equals(shift)).toList();
        // check shift
        Schedule scheduleOfShift = !schedules.isEmpty() ? schedules.get(0) : null;

        LocalDateTime timeCheckIn = LocalDateTime.now();
        if (scheduleOfShift == null || scheduleOfShift.getTimeStart() == null) {
            return ApiResponseHelper.fallback(new Exception("No time of this schedule"));
        }
        LocalDateTime timeStartToCheckIn = LocalDateTime.now().withHour(scheduleOfShift.getTimeStart().getHour()).withMinute(scheduleOfShift.getTimeStart().getMinute()).withSecond(scheduleOfShift.getTimeStart().getSecond());
        long late = timeStartToCheckIn.atZone(ZoneOffset.UTC).toInstant().toEpochMilli() - timeCheckIn.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        TimeKeeping newTime = new TimeKeeping();
        newTime.setCheckInTime(timeCheckIn);
        newTime.setCheckoutTime(LocalDateTime.now());
        newTime.setCheckedOutToDay(false);
        newTime.setUserChecked(userRepository.findById(userId).orElse(null));
        newTime.setLate(late > 0 ? 0 : Math.abs(late));
        newTime.setShift(shift);
        newTime.setWorkday(workDay);
        newTime.setStatus(TimeKeepingStatus.CHECKIN);
        return ApiResponseHelper.success(checkInRepository.save(newTime));
    }

    @Override
    public ResponseEntity<?> checkout(String userId, TimeKeepShift shift) throws Exception {
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        WorkDay workDay = mapToWorkDay(dayOfWeek);
        List<Schedule> schedules = scheduleRepository.findAllByWorkDay(workDay).stream().filter(s -> s.getShift().equals(shift)).toList();
        // check shift
        Schedule scheduleOfShift = !schedules.isEmpty() ? schedules.get(0) : null;

        LocalDateTime timeCheckOut = LocalDateTime.now();
        if (scheduleOfShift == null || scheduleOfShift.getTimeStart() == null) {
            return ApiResponseHelper.fallback(new Exception("No time of this schedule"));
        }
        LocalDateTime timeStartToCheckOut = LocalDateTime.now().withHour(scheduleOfShift.getTimeEnd().getHour()).withMinute(scheduleOfShift.getTimeEnd().getMinute()).withSecond(scheduleOfShift.getTimeEnd().getSecond());
        long overtime = timeStartToCheckOut.atZone(ZoneOffset.UTC).toInstant().toEpochMilli() - timeCheckOut.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        User userCheck = userRepository.findById(userId).orElse(null);
        TimeKeeping newTime = new TimeKeeping();
        newTime.setCheckInTime(LocalDateTime.now());
        newTime.setCheckoutTime(timeCheckOut);
        newTime.setCheckedOutToDay(true);
        newTime.setUserChecked(userCheck);
        newTime.setOverTime(overtime >= 0 ? 0 : Math.abs(overtime));
        newTime.setLate(0);
        newTime.setShift(shift);
        newTime.setWorkday(workDay);
        newTime.setStatus(TimeKeepingStatus.CHECKOUT);
        if (userCheck != null) {
            userCheck.setCheckInPoint(userCheck.getCheckInPoint() + SCORE_PER_TIME);
            if (userCheck.getCheckInPoint() > 5) {
                userCheck.setCheckInPoint(5);
            }
            kpiHistoryService.createNewFromOtherService(String.format("Cộng %s điểm từ chấm công", SCORE_PER_TIME), userCheck);
        }
        return ApiResponseHelper.success(checkInRepository.save(newTime));
    }

    private WorkDay mapToWorkDay(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY -> {
                return WorkDay.MONDAY;
            }
            case FRIDAY -> {
                return WorkDay.FRIDAY;
            }
            case THURSDAY -> {
                return WorkDay.THURSDAY;
            }
            case SUNDAY -> {
                return WorkDay.SUNDAY;
            }
            case SATURDAY -> {
                return WorkDay.SATURDAY;
            }
            case WEDNESDAY -> {
                return WorkDay.WEDNESDAY;
            }
            case TUESDAY -> {
                return WorkDay.TUESDAY;
            }
        }
        return WorkDay.MONDAY;
    }

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) throws Exception {
        return ApiResponseHelper.success(checkInRepository.findAll(pageable));
    }

    @Override
    public ResponseEntity<?> getByUser(Pageable pageable, String userId) throws Exception {
        User user = userRepository.findById(userId).orElse(null);
        return ApiResponseHelper.success(checkInRepository.findAllByUserChecked(pageable, user));
    }

    @Override
    public ResponseEntity<?> getByUserManager(Pageable pageable, String userId) throws Exception {
        User manager = userRepository.findById(userId).orElseThrow(() -> new Exception(Messages.USER_NOT_FOUND));
        Set<User> users = manager.getStaffs();
        return ApiResponseHelper.success(checkInRepository.findAllByUserCheckedIn(pageable, users));
    }
}
