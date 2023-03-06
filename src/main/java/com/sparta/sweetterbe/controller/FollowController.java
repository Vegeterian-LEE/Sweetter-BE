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

    @PostMapping("/{followUsername}")
    public StatusResponseDto<FollowResponseDto> makeFollow(
            @PathVariable String followUsername,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        return StatusResponseDto.success(followService.makeFollow(followUsername, userDetails));
    }

    @PutMapping("/approve/{followerUsername}")
    public StatusResponseDto<FollowResponseDto> approveFollow(
            @PathVariable String followerUsername,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        followService.approveFollow(followerUsername, userDetails);
        return StatusResponseDto.success(followService.approveFollow(followerUsername, userDetails));
    }

    @DeleteMapping("/deny/{followerUsername}")
    public StatusResponseDto<String> denyFollow(
            @PathVariable String followerUsername,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        followService.denyFollow(followerUsername, userDetails);
        return StatusResponseDto.success("삭제 성공");
    }

}
