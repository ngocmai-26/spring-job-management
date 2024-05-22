package com.job_manager.mai.repository;

import com.job_manager.mai.model.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);

    boolean existsByRoleName(String name);

    Page<Role> findAllByRoleNameContaining(Pageable pageable, String name);

    @Modifying
    @Transactional
    @Query(value = "delete from permission_roles where role_id=:id", nativeQuery = true)
    void deleteRolePerm(long id);
}
