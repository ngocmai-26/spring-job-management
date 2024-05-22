package com.job_manager.mai.schedule;

import com.job_manager.mai.contrains.ScheduleType;
import com.job_manager.mai.model.Plan;
import com.job_manager.mai.model.PlanTask;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlanScheduleCreator {

    private final TaskScheduler taskScheduler;
    private final int MAX_OF_USER = 3;
    private List<PlanTask> runningTask = new ArrayList<>();

    public void createSchedule(PlanTask planTask) {
        ScheduleType scheduleType = planTask.getPlan().getPlanDetail().getPlanSchedules().get(0).getScheduleType();
        CronTrigger cronTrigger = null;
        int[] minMaxSchedule = planTask.getPlan().getPlanDetail().getMaxMinSchedule();
        switch (scheduleType) {
            case DAY -> {
                cronTrigger = new CronTrigger(String.format("0 0 0 %d-%d * ?", minMaxSchedule[0], minMaxSchedule[1]));
            }
            case MONTH -> {
                cronTrigger = new CronTrigger(String.format("0 0 0 1 %d-%d ?", minMaxSchedule[0], minMaxSchedule[1]));
            }
            case YEAR -> {
                cronTrigger = new CronTrigger(String.format("0 0 0 0 * ? %d-%d", minMaxSchedule[0], minMaxSchedule[1]));
            }
        }
        if (cronTrigger != null) {
            planTask.setScheduledFuture(taskScheduler.schedule(planTask.getTask(), cronTrigger));
            runningTask.add(planTask);
        }
    }

    public void pauseTask(String id) {

    }

    public void resumeTask(String id) {

    }

    public void cancelPlan(String id) {
        PlanTask planTask = runningTask.stream().filter(p -> p.getId().equals(id)).toList().get(0);
        if (planTask != null) {
            planTask.getScheduledFuture().cancel(false);
        }
    }

    public boolean checkCanAddByUser(PlanTask planTask) {
        int count = 0;
        for (PlanTask planTask1 : runningTask) {
            if (planTask1.getPlan().getCreator().getId().equals(planTask.getPlan().getCreator().getId())) {
                count++;
            }
        }
        if (count + 1 > MAX_OF_USER) {
            return false;
        }
        return true;
    }
}
