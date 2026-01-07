package com.example.StudyPost.comment.repository;

import com.example.StudyPost.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
