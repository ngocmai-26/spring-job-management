package com.job_manager.mai.model;

import jakarta.persistence.*;
import lombok.Data;

import javax.annotation.processing.Generated;

@Entity
@Data
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @ManyToOne
    private User to;

    private String content;

    private int type;

    @ManyToOne
    private User from;

    private String dataId;

    private String image;
    private boolean isRead;
}
