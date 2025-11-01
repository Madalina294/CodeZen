package com.app_template.App_Template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app_template.App_Template.entity.Review;
import com.app_template.App_Template.entity.ReviewComment;

/**
 * Repository interface for ReviewComment entity operations.
 */
@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    /**
     * Find all comments for a specific review, ordered by timestamp.
     */
    List<ReviewComment> findByReviewOrderByTimestampAsc(Review review);
}

