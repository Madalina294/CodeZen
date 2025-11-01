package com.app_template.App_Template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app_template.App_Template.entity.Project;
import com.app_template.App_Template.entity.User;

/**
 * Repository interface for Project entity operations.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Find all projects owned by a specific user.
     */
    List<Project> findByOwnerOrderByCreatedAtDesc(User owner);

    /**
     * Find a project by ID and owner (for security).
     */
    Optional<Project> findByIdAndOwner(Long id, User owner);

    /**
     * Check if a project exists and belongs to the user.
     */
    boolean existsByIdAndOwner(Long id, User owner);
}