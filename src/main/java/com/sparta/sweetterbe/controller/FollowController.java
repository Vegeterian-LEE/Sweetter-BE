package com.sparta.sweetterbe.controller;

import com.sparta.sweetterbe.dto.FollowResponseDto;
import com.sparta.sweetterbe.dto.StatusResponseDto;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.service.FollowService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{postId}")
    public StatusResponseDto<FollowResponseDto> makeFollow(
            @PathVariable Long postId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return StatusResponseDto.success(followService.makeFollow(postId, userDetails));
    }

    @PutMapping("/approve/{postId}")
    public StatusResponseDto<FollowResponseDto> approveFollow(
            @PathVariable Long postId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        followService.approveFollow(postId, userDetails);
        return StatusResponseDto.success(followService.makeFollow(postId, userDetails));
    }

    @DeleteMapping("/deny/{postId}")
    public StatusResponseDto<String> denyFollow(
            @PathVariable Long postId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        followService.denyFollow(postId, userDetails);
        return StatusResponseDto.success("삭제 성공");
    }

}
