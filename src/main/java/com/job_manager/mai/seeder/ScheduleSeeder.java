package com.job_manager.mai.seeder;

import com.job_manager.mai.contrains.TimeKeepShift;
import com.job_manager.mai.contrains.WorkDay;
import com.job_manager.mai.model.Schedule;
import com.job_manager.mai.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleSeeder implements CommandLineRunner, Ordered {
    private final ScheduleRepository scheduleRepository;

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(WorkDay.values()).forEach((wd) -> {
            Arrays.stream(TimeKeepShift.values()).forEach(timeKeepShift -> {
                List<Schedule> schedule = scheduleRepository.findAllByWorkDay(wd);
                if (!schedule.isEmpty() && schedule.size() >= 3) {
                    return;
                }
                Schedule newSchedule = new Schedule();
                newSchedule.setWorkDay(wd);
                newSchedule.setShift(timeKeepShift);
                switch (timeKeepShift) {
                    case MORNING -> {
                        newSchedule.setTimeStart(LocalTime.of(7, 0, 0));
                        newSchedule.setTimeEnd(LocalTime.of(11, 0, 0));
                    }
                    case AFTERNOON -> {
                        newSchedule.setTimeStart(LocalTime.of(13, 0, 0));
                        newSchedule.setTimeEnd(LocalTime.of(17, 0, 0));
                    }
                    case NIGHT -> {
                        newSchedule.setTimeStart(LocalTime.of(17, 0, 0));
                        newSchedule.setTimeEnd(LocalTime.of(21, 30, 0));
                    }
                }
                scheduleRepository.save(newSchedule);
            });
        });
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
