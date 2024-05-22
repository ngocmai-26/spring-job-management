package com.job_manager.mai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.job_manager.mai.contrains.MessageStatus;
import com.job_manager.mai.contrains.MessageType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "messages")
public class Message {

    @Id
    private String id;

    @Column(nullable = false)
    private String content;

    @Nullable
    @OneToMany
    private Set<Media> media = new HashSet<>();

    @ManyToOne
    private Room room;

    @ManyToOne
    private User sender;

    private int status = MessageStatus.DISPLAY.getVal();

    private LocalDateTime sentAt = LocalDateTime.now();

    public Message() {
        this.id = UUID.randomUUID().toString();
    }

    private int messageTypeInRoom = MessageType.NORMAL_MESSAGE.getVal();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "message_readers", joinColumns = {@JoinColumn(name = "message_id")}, inverseJoinColumns = {@JoinColumn(name = "member_id")})
    private Set<User> readers = new HashSet<>();

    public void addMedia(Media media) {
        this.media.add(media);
    }

    public void removeMedia(Media media) {
        this.media.remove(media);
    }

    public void clearMedia() {
        this.media.clear();
    }
}
