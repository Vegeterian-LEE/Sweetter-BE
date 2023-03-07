package com.sparta.sweetterbe.controller;

import com.sparta.sweetterbe.dto.*;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    public StatusResponseDto<String> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return StatusResponseDto.success(userService.signup(signupRequestDto));
    }

    //로그인 후 페이지 구성에 사용할 유저정보 리턴 - 정환
    @PostMapping("/login")
    public StatusResponseDto<UserResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                         @Parameter(hidden = true) HttpServletResponse response){
        return StatusResponseDto.success(userService.login(loginRequestDto, response));
    }

    //내 정보 변경
    @PutMapping("/settings/profile")
    public StatusResponseDto<UserResponseDto> updateProfile(@RequestBody UserRequestDto userRequestDto,
                                                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return StatusResponseDto.success(userService.updateProfile(userRequestDto, userDetails));
    }
    // 현재는 본인이 팔로우 하지 않은 모든 유저가 나타나게 되어 있음
    @GetMapping("/list")
    public StatusResponseDto<List<UserListDto>> getUserList(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails){
        return StatusResponseDto.success(userService.getUserList(userDetails));
    }

    @GetMapping("/search/{searchWord}")
    public StatusResponseDto<List<UserResponseDto>> searchUser(@PathVariable String searchWord, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails){
        return StatusResponseDto.success(userService.searchUser(searchWord, userDetails));
    }


}
