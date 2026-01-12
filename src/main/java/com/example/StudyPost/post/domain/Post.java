package com.example.StudyPost.post.domain;

import com.example.StudyPost.comment.domain.Comment;
import com.example.StudyPost.global.entity.BaseEntity;
import com.example.StudyPost.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 200)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    private String imageUrl; // S3 이미지 URL을 저장할 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    private Post(String title, String content, String imageUrl, User user){
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
