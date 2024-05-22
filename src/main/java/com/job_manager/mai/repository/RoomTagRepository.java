package com.job_manager.mai.repository;

import com.job_manager.mai.model.Room;
import com.job_manager.mai.model.RoomTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTagRepository extends JpaRepository<RoomTag, Long> {

    Optional<RoomTag> findByName(String name);
}
