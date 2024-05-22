package com.job_manager.mai.repository;

import com.job_manager.mai.model.Notification;
import com.job_manager.mai.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByTo(Pageable pageable, User user);
}
