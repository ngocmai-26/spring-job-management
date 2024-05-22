package com.job_manager.mai.service.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolService {
    private static ExecutorService I;

    public static int LIMIT_EXECUTOR_THREAD = 10;

    public static ExecutorService gI() {
        if (I == null) {
            I = Executors.newFixedThreadPool(LIMIT_EXECUTOR_THREAD);
        }
        return I;
    }
}
