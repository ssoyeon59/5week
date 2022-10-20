package com.sparta.project.service;


import com.sparta.project.entity.Member;
import com.sparta.project.entity.UserDetailsImpl;
import com.sparta.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
    Optional<Member> member = memberRepository.findByNickname(nickname);
    return member
        .map(UserDetailsImpl::new)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
  }
}
