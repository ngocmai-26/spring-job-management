package com.job_manager.mai.request.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CreateRoomRequest extends RoomRequest {

    @NotBlank
    private String roomName;

    @NotNull
    private int maxCountMember;

    private Set<Long> roomTagsId = new HashSet<>();
    private Set<String> initMember = new HashSet<>();
}
