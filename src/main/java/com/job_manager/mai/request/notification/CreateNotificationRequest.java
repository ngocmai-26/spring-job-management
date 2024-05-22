package com.job_manager.mai.request.notification;

import com.job_manager.mai.model.User;
import lombok.Data;

@Data
public class CreateNotificationRequest {
    private User from;

    private User to;

    private String content;

    private int type;
    private String dataId;
}
