package com.example.StudyPost.post.service;

import com.example.StudyPost.global.exception.CustomException;
import com.example.StudyPost.global.exception.ErrorCode;
import com.example.StudyPost.global.s3.S3UploaderService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final S3UploaderService s3UploaderService; // S3UploaderService 주입

    @Transactional
    public Long create(PostCreateRequestDto dto, MultipartFile image, User user) throws IOException {
        String imageUrl = null;
        // 이미지가 존재하면 S3에 업로드하고 URL을 받아옴
        if (image != null && !image.isEmpty()) {
            imageUrl = s3UploaderService.upload(image, "posts");
        }

        Post post = Post.builder()
                .title(dto.title())
                .content(dto.content())
                .imageUrl(imageUrl) // 이미지 URL 저장
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
        return posts.map(PostResponseDto::from);
    }

    @Transactional
    public Long update(Long postId, PostUpdateRequestDto dto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }

        post.update(dto.title(), dto.content());
        return post.getId();
    }

    @Transactional
    public void delete(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(!post.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }

        postRepository.delete(post);
    }
}
