package com.sparta.sweetterbe.controller;


import com.sparta.sweetterbe.dto.CommentRequestDto;
import com.sparta.sweetterbe.dto.CommentResponseDto;
import com.sparta.sweetterbe.dto.IsLikeResponseDto;
import com.sparta.sweetterbe.dto.StatusResponseDto;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.service.CommentService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{postId}")
    public StatusResponseDto<CommentResponseDto> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto,
                                                               @Parameter(hidden = true)@AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(postId, commentRequestDto, userDetails);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public StatusResponseDto<String> deleteComment(@PathVariable Long commentId,
                                                   @Parameter(hidden = true)@AuthenticationPrincipal UserDetailsImpl userDetails) throws AuthenticationException {
        return commentService.deleteComment(commentId, userDetails);
    }

    // 댓글 좋아요
    @PostMapping("/like/{commentId}")
    public StatusResponseDto<IsLikeResponseDto> likeComment(@PathVariable Long commentId,
                                                            @Parameter(hidden = true)@AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.likeComment(commentId, userDetails);
    }

}
