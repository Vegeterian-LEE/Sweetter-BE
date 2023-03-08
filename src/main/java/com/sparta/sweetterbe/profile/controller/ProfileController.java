package com.sparta.sweetterbe.profile.controller;


import com.sparta.sweetterbe.profile.dto.ProfileResponseDto;
import com.sparta.sweetterbe.dto.StatusResponseDto;
import com.sparta.sweetterbe.profile.dto.ProfileSecondListResponsDto;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;

    // 프로필에 들어가는 post리스트 호출 기능
    //트윗한 리스트와, 리트윗 리스트 합쳐서 나오는 api
    @GetMapping("/tweetlist/{userId}")
    public StatusResponseDto<List<ProfileResponseDto>> getTweetList(
            @PathVariable Long userId, @Parameter(hidden = true)@AuthenticationPrincipal UserDetailsImpl userDetails){
        return StatusResponseDto.success(profileService.getTweetList(userId, userDetails));
    }

    //내가 쓴 댓글이 달린 글과 리트윗과 내가 쓴글까지 나오는 글
    @GetMapping("/tweetlistAndCommentlist/{userId}")
    public StatusResponseDto<List<ProfileResponseDto>> getUserList(
            @PathVariable Long userId, @Parameter(hidden = true)@AuthenticationPrincipal UserDetailsImpl userDetails){
        return StatusResponseDto.success(profileService.getUserList(userId, userDetails));
    }

    //미디어만 들어간 객체 배열 가져오기
    @GetMapping("/medialist/{userId}")
    public StatusResponseDto<List<ProfileResponseDto>> getMediaList(
            @PathVariable Long userId, @Parameter(hidden = true)@AuthenticationPrincipal UserDetailsImpl userDetails){
        return StatusResponseDto.success(profileService.getMediaList(userId, userDetails));
    }

    //좋아요 한 게시글만 보여주기
    @GetMapping("/likelist/{userId}")
    public StatusResponseDto<List<ProfileResponseDto>> getLikeList(
            @PathVariable Long userId, @Parameter(hidden = true)@AuthenticationPrincipal UserDetailsImpl userDetails){
        return StatusResponseDto.success(profileService.getLikeList(userId, userDetails));
    }
}
