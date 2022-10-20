package com.sparta.project.repository;

import com.sparta.project.entity.PostLike;
import com.sparta.project.entity.Member;
import com.sparta.project.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//@Repository
public interface
LikeRepository extends JpaRepository<PostLike, Long> {
//    Optional<PostLike> findByPostAndMember(post, member);
//    List<PostLike> findByPostId(Long post_id);
    Long countByPostId(Long postId);
    Optional<PostLike> findByPostAndMember(Optional<Post> post, Optional<Member> member);
}

