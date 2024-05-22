package com.job_manager.mai.request.room;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class DeleteRoomRequest extends RoomRequest {
    Set<String> deleteIds = new HashSet<>();
}
