package com.job_manager.mai.service.room;

import com.job_manager.mai.request.room.*;
import com.job_manager.mai.service.inteface.IBaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RoomService extends IBaseService<RoomRequest, CreateRoomRequest, UpdateRoomRequest, DeleteRoomRequest, String> {

    ResponseEntity<?> getByMember(String memberId);

    ResponseEntity<?> getMessageById(Pageable pageable, String roomId);

    ResponseEntity<?> getAllByUser(Pageable pageable, String userId);

    ResponseEntity<?> getAllTag();

    ResponseEntity<?> addMember(String roomId, AddMemberToRoomRequest request) throws Exception;

    ResponseEntity<?> removeMember(String roomId, AddMemberToRoomRequest request) throws Exception;
}
