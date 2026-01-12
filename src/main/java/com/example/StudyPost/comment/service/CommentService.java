package com.example.StudyPost.comment.service;

import com.example.StudyPost.comment.domain.Comment;
import com.example.StudyPost.comment.dto.CommentCreateRequestDto;
import com.example.StudyPost.comment.dto.CommentUpdateRequestDto;
import com.example.StudyPost.comment.repository.CommentRepository;
import com.example.StudyPost.global.exception.CustomException;
import com.example.StudyPost.global.exception.ErrorCode;
import com.example.StudyPost.post.domain.Post;
import com.example.StudyPost.post.repository.PostRepository;
import com.example.StudyPost.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 댓글 작성
    @Transactional
    public Long createComment(Long postId, CommentCreateRequestDto dto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(dto.content())
                .post(post)
                .user(user)
                .build();

        return commentRepository.save(comment).getId();
    }

    // 댓글 수정
    @Transactional
    public Long updateComment(Long commentId, CommentUpdateRequestDto dto, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 작성자 검증(작성한 유저와 현재 수정할려고 하는 유저가 일치한지 확인)
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 에러
        }

        comment.update(dto.content());
        return comment.getId();
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 작성자 검증
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }
}
