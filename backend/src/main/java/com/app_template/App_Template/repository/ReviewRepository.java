package com.app_template.App_Template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app_template.App_Template.entity.Project;
import com.app_template.App_Template.entity.Review;
import com.app_template.App_Template.entity.User;

/**
 * Repository interface for Review entity operations.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Find all reviews for a specific project, ordered by timestamp.
     */
    List<Review> findByProjectOrderByTimestampDesc(Project project);

    /**
     * Find a specific review by ID and project.
     */
    Optional<Review> findByIdAndProject(Long id, Project project);

    /**
     * Find all reviews created by a specific user.
     */
    List<Review> findByUserOrderByTimestampDesc(User user);
}