package com.job_manager.mai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.job_manager.mai.contrains.MessageStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "rooms")
public class Room {
    public static int MAX_ROOM_MEMBER = 50;
    @jakarta.persistence.Id
    private String Id;

    @Column
    private String roomName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "member_rooms", joinColumns = {@JoinColumn(name = "room_id")}, inverseJoinColumns = {@JoinColumn(name = "member_id")})
    private Set<User> members = new HashSet<>();

    private int maxMemberCount = MAX_ROOM_MEMBER;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "banned_user_rooms", joinColumns = {@JoinColumn(name = "room_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> bannedUser = new HashSet<>();

    @ManyToOne
    private User leader;

    @ManyToMany
    @JoinTable(name = "room_sub_leaders", joinColumns = {@JoinColumn(name = "room_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> subLeader = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rooms_tags", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Set<RoomTag> tags = new HashSet<>();

    @OneToMany
    private Set<Media> media = new HashSet<>();

    public Room() {
        this.Id = UUID.randomUUID().toString();
    }

    public void addMember(User member) {
        this.members.add(member);
    }

    public void addTag(RoomTag roomTag) {
        this.tags.add(roomTag);
    }

    public void addMedia(Media media) {
        this.media.add(media);
    }

    public void removeMedia(Media media) {
        this.media.remove(media);
    }

    public void clearMedia() {
        this.media.clear();
    }

    public void removeMember(User user) {
        this.members.remove(user);
    }
}
