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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    // 게시글 작성 (이미지 포함) - 수정된 방식
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal SecurityUser securityUser
    ) throws IOException {
        User user = getUser(securityUser);
        // DTO를 컨트롤러에서 직접 생성
        PostCreateRequestDto dto = new PostCreateRequestDto(title, content);
        Long postId = postService.create(dto, image, user);
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
        Page<PostResponseDto> responseDtos = postService.getAll(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ok(responseDtos));
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

    private User getUser(SecurityUser securityUser) {
        return userRepository.findById(securityUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }
}
