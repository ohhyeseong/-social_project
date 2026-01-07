package com.example.StudyPost.post.controller;

import com.example.StudyPost.global.exception.CustomException;
import com.example.StudyPost.global.exception.ErrorCode;
import com.example.StudyPost.global.response.ApiResponse;
import com.example.StudyPost.global.security.custom.SecurityUser;
import com.example.StudyPost.post.dto.PostCreateRequestDto;
import com.example.StudyPost.post.dto.PostResponseDto;
import com.example.StudyPost.post.dto.PostUpdateRequestDto;
import com.example.StudyPost.post.service.PostService;
import com.example.StudyPost.user.domain.User;
import com.example.StudyPost.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPost(
            @Valid @RequestBody PostCreateRequestDto dto,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        User user = getUser(securityUser);
        Long postId = postService.create(dto, user);
        return ResponseEntity.ok(ApiResponse.ok(postId));
    }

    // 게시글 단건 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPost(@PathVariable Long postId) {
        PostResponseDto responseDto = postService.getById(postId);
        return ResponseEntity.ok(ApiResponse.ok(responseDto));
    }

    // 게시글 목록 조회 (페이징, 검색)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponseDto>>> getAllPosts(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<PostResponseDto> responseDto = postService.getAll(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ok(responseDto));
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<Long>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequestDto dto,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        User user = getUser(securityUser);
        Long updatedPostId = postService.update(postId, dto, user);
        return ResponseEntity.ok(ApiResponse.ok(updatedPostId));
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        User user = getUser(securityUser);
        postService.delete(postId, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    // SecurityUser -> User 엔티티 변환 헬퍼 메서드
    private User getUser(SecurityUser securityUser) {
        return userRepository.findById(securityUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND)); // User Not Found 에러코드가 없어서 일단 이걸로 대체하거나 추가 필요
    }
}
