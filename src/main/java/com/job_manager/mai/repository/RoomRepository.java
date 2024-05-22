package com.job_manager.mai.repository;

import com.job_manager.mai.model.Room;
import com.job_manager.mai.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    Page<Room> findByRoomNameContaining(Pageable pageable,String name);
    List<Room> findAllByRoomNameAndLeader(String roomName, User leader);
}
