package com.sparta.project.service;

import com.sparta.project.dto.request.LikeRequestDto;
import com.sparta.project.dto.response.ResponseDto;
import com.sparta.project.entity.Member;
import com.sparta.project.entity.Post;
import com.sparta.project.entity.PostLike;
import com.sparta.project.repository.LikeRepository;
import com.sparta.project.repository.MemberRepository;
import com.sparta.project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    private final LikeRepository likeRepository;

    @Transactional
    public ResponseDto<?> createPostLike(LikeRequestDto requestDto, HttpServletRequest request) {
        //게시글 존재 여부
        Optional<Post> post = postRepository.findById(requestDto.getPostId());
        if (post.isEmpty()) {
            return ResponseDto.fail("NOT_FOUND",
                    "존재하지 않는 게시글 id 입니다.");
        }

        //로그인했는지 확인
        Optional<Member> member = memberRepository.findById(requestDto.getMemberId());
        if (member.isEmpty()) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if(likeRepository.findByPostAndMember(post, member).isEmpty()) {
            PostLike postLike = PostLike.builder()
                    .post(post.get())
                    .member(member.get())
                    .build();
            likeRepository.save(postLike);
            return ResponseDto.success("좋아요");
        }
        return ResponseDto.success("이미 좋아요를 눌렀습니다.");
    }

}


//    @Transactional
//    public ResponseDto<?> deletePostLike(LikeRequestDto requestDto) {
//        //회원인지 확인
//        Optional<Member> membercheck = memberRepository.findById(requestDto.getMemberId());
//        if (membercheck.isEmpty()) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        //게시글 존재 여부
//        Optional<Post> postCheck = postRepository.findById(requestDto.getPostId());
//        if (postCheck.isEmpty()) {
//            return ResponseDto.fail("NOT_FOUND",
//                    "존재하지 않는 게시글 id 입니다.");
//        }
//
//        Optional<PostLike> optionalPostLike = likeRepository.findByPostIdAndMemberId(
//                requestDto.getPostId(), requestDto.getMemberId());
//        //좋아요가 눌려있지 않다면
//        if (optionalPostLike.isEmpty()) {
//            return ResponseDto.fail("BAD_REQUEST", "좋아요를 누르지 않으셨습니다.");
//        }
//
//        likeRepository.delete(optionalPostLike.get());
//
//// 좋아요 눌린 해당 포스트의 좋아요 컬럼에 -1
////        post.getLikeNum() - 1);
//
//        return ResponseDto.success("unlike success.");
//    }
//}

