package com.job_manager.mai.request.room;

import com.job_manager.mai.contrains.RoleInRoom;
import lombok.Data;

@Data
public class AddMemberToRoomRequest {
    private String userId;
    private String adderId;
    private RoleInRoom roleInRoom = RoleInRoom.MEMBER;
}
