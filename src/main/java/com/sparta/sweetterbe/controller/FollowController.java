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

    @PostMapping("/{followerUsername}")
    public StatusResponseDto<FollowResponseDto> makeFollow(
            @PathVariable String followerUsername,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        return StatusResponseDto.success(followService.makeFollow(followerUsername, userDetails));
    }

    @DeleteMapping ("/{followerUsername}")
    public StatusResponseDto<String> deleteFollow(
            @PathVariable String followerUsername,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        followService.deleteFollow(followerUsername, userDetails);
        return StatusResponseDto.success("삭제성공");
    }



    @PutMapping("/approve/{followingUsername}")
    public StatusResponseDto<FollowResponseDto> approveFollow(
            @PathVariable String followingUsername,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        followService.approveFollow(followingUsername, userDetails);
        return StatusResponseDto.success(followService.approveFollow(followingUsername, userDetails));
    }

    @DeleteMapping("/deny/{followingUsername}")
    public StatusResponseDto<String> denyFollow(
            @PathVariable String followingUsername,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        followService.denyFollow(followingUsername, userDetails);
        return StatusResponseDto.success("삭제 성공");
    }

}
