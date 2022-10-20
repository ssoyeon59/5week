package com.sparta.project.repository;

import com.sparta.project.entity.Comment;
import com.sparta.project.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPost(Post post);
}
