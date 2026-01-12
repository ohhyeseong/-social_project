package com.example.StudyPost.comment.domain;

import com.example.StudyPost.global.entity.BaseEntity;
import com.example.StudyPost.post.domain.Post;
import com.example.StudyPost.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 내용
    @Column(nullable = false, length = 500)
    private String content;

    // [N:1 관계] 댓글(N) : 사용자(1)
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 (실제 user 데이터가 필요할 때 조회)
    // JoinColumn(name = "user_id"): 외래키(FK) 컬럼명을 'user_id로 지정
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User entity 에서 가져오겠다는 뜻
    
    // [N:1 관계] 댓글(N) : 게시글(1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder// 여기서 댓글 내용만 빌드 하면 안되는 이유 찾기
    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    public void update(String content) {
        this.content = content;
    }
}
