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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    //회원가입 완료 후 반환값 > FE와 협의 필요 - 정환
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
    //리턴 값 FE와 협의 필요 - 정환
    @PostMapping("/checkpw")
    public StatusResponseDto<String> checkPassword(@RequestBody PasswordRequestDto passwordRequestDto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return StatusResponseDto.success(userService.checkPassword(passwordRequestDto, userDetails));
    }
    //내 정보 변경
    @PutMapping("/settings/profile")
    public StatusResponseDto<UserResponseDto> updateProfile(@RequestBody UserRequestDto userRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return StatusResponseDto.success(userService.updateProfile(userRequestDto, userDetails));
    }
}
