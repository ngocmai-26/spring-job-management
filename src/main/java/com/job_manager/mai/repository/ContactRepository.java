package com.job_manager.mai.repository;

import com.job_manager.mai.model.Contact;
import com.job_manager.mai.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findAllByOwnerAndStatus(Pageable pageable, User owner, int status);

    @Query(value = "select * from contacts where (owner_id = :user or relate_id = :user) and status = :status",nativeQuery = true)
    Page<Contact> findAllByOwnerOrRelateAndStatus(Pageable pageable, String user, int status);

    Page<Contact> findAllByRelateAndStatus(Pageable pageable, User relate, int status);

    Optional<Contact> findByOwnerAndRelate(User owner, User relate);
}
