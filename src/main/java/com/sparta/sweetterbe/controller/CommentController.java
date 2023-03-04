package com.sparta.sweetterbe.controller;


import com.sparta.sweetterbe.dto.CommentResponseDto;
import com.sparta.sweetterbe.dto.StatusResponseDto;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/comment/{id}")
    public StatusResponseDto<CommentResponseDto> createComment(@PathVariable Long id, String content, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(id, content, userDetails);
    }

    //댓글 삭제
    @DeleteMapping("/comment/{id}")
    public StatusResponseDto<String> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws AuthenticationException {
        return commentService.deleteComment(id, userDetails);
    }

    // 댓글 좋아요
    @PostMapping("/comment/like/{id}")
    public StatusResponseDto<String> likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.likeComment(id, userDetails);
    }

}
