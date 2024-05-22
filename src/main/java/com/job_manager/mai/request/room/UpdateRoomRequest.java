package com.job_manager.mai.request.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateRoomRequest extends RoomRequest {
    @NotBlank
    private String roomName;

    @NotNull
    private int maxCountMember;

    private Set<String> initMember;
}
