package com.sparta.project.entity;

import com.sparta.project.dto.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int likenum;

    //fetch는 읽어오기 전략, LAZY는 필요할 때(사용하는 구간에 트랜젝션), EAGER은 항상
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    public void deleteLikenum() {
        this.likenum--;
    }

    public void addlikenum() {
        this.likenum++;
    }
}