package com.example.StudyPost.post.service;

import com.example.StudyPost.global.exception.CustomException;
import com.example.StudyPost.global.exception.ErrorCode;
import com.example.StudyPost.post.domain.Post;
import com.example.StudyPost.post.dto.PostCreateRequestDto;
import com.example.StudyPost.post.dto.PostResponseDto;
import com.example.StudyPost.post.dto.PostUpdateRequestDto;
import com.example.StudyPost.post.repository.PostRepository;
import com.example.StudyPost.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long create(PostCreateRequestDto dto, User user){
        Post post = Post.builder()
                .title(dto.title())
                .content(dto.content())
                .user(user)
                .build();

        return postRepository.save(post).getId();
    }

    public PostResponseDto getById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return PostResponseDto.from(post);
    }

    public Page<PostResponseDto> getAll(String keyword, Pageable pageable){
        Page<Post> posts;
        if(keyword == null || keyword.isBlank()){
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByTitleContaining(keyword, pageable);
        }
        // 엔티티 Page를 DTO Page로 변환
        return posts.map(PostResponseDto::from);
    }

    @Transactional
    public Long update(Long postId, PostUpdateRequestDto dto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.VALIDATION_ERROR); // 추후 권한 관련 에러코드로 변경 권장
        }

        post.update(dto.title(), dto.content());
        return post.getId();
    }

    @Transactional
    public void delete(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 수정: User 객체가 아닌 ID끼리 비교
        if(!post.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }

        postRepository.delete(post);
    }
}
