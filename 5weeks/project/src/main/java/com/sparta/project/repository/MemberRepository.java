package com.sparta.project.repository;


import java.util.Optional;

import com.sparta.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
}
