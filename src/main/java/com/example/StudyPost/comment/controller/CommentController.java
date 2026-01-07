package com.example.StudyPost.comment.controller;

import com.example.StudyPost.comment.dto.CommentCreateRequestDto;
import com.example.StudyPost.comment.dto.CommentUpdateRequestDto;
import com.example.StudyPost.comment.service.CommentService;
import com.example.StudyPost.global.exception.CustomException;
import com.example.StudyPost.global.exception.ErrorCode;
import com.example.StudyPost.global.response.ApiResponse;
import com.example.StudyPost.global.security.custom.SecurityUser;
import com.example.StudyPost.user.domain.User;
import com.example.StudyPost.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    // 댓글 작성
    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<Long>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequestDto dto,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        User user = getUser(securityUser);
        Long commentId = commentService.createComment(postId, dto, user);
        return ResponseEntity.ok(ApiResponse.ok(commentId));
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Long>> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto dto,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        User user = getUser(securityUser);
        Long updatedCommentId = commentService.updateComment(commentId, dto, user);
        return ResponseEntity.ok(ApiResponse.ok(updatedCommentId));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        User user = getUser(securityUser);
        commentService.deleteComment(commentId, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    private User getUser(SecurityUser securityUser) {
        // 로그인하지 않은 사용자의 경우 securityUser가 null일 수 있음
        if (securityUser == null) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR); // 또는 UNAUTHORIZED
        }
        return userRepository.findById(securityUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND)); // USER_NOT_FOUND 에러코드 추가 권장
    }
}
