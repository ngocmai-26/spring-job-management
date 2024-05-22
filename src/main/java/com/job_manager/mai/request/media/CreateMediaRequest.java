package com.job_manager.mai.request.media;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateMediaRequest {
    private String mediaLink;

    private String mediaType;
    private String senderId;

    private LocalDateTime sentAt;

    private String mediaName;
    private String mediaSize;
}
