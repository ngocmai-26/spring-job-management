package com.job_manager.mai.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "medias")
public class Media {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String mediaLink;

    private String mediaType;
    private String senderId;

    private LocalDateTime sentAt = LocalDateTime.now();

    private String mediaName;
    private String mediaSize;

}
