package com.sparta.project.repository;

import com.sparta.project.entity.Member;
import com.sparta.project.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
    List<Post> findAllByMember(Member member);
}