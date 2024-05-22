package com.job_manager.mai.repository;

import com.job_manager.mai.model.Message;
import com.job_manager.mai.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    Page<Message> findAllByRoomOrderBySentAtDesc(Pageable pageable, Room room);
    Message findFirstByRoomOrderBySentAtDesc(Room room);
}
