package com.sparta.sweetterbe.controller;

import com.sparta.sweetterbe.dto.*;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.service.S3UploadService;
import com.sparta.sweetterbe.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final S3UploadService s3UploadService;

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

    @GetMapping("/info")
    public StatusResponseDto<UserInfoResponseDto> userInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return StatusResponseDto.success(userService.getUserInfo(userDetails));
    }

    //json과 multipart/form 둘 다 쓰려면 content-type이 구별이 되야하는 데
    //postman은 구분 지을 수 있도록 설정이 가능하지만 swagger는 그렇지 않다
    @PutMapping(consumes = "multipart/form-data", value="/update")
    public StatusResponseDto<UserResponseDto> updateInfo(
            @RequestPart(value="file",required = false) List<MultipartFile> file,
            @Valid @RequestPart(value="key") UserUpdateDto userUpdateDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
            )throws IOException {
        return StatusResponseDto.success(userService.updateUserInfo(userDetails,s3UploadService.uploadFiles(file,"sweetter"),userUpdateDto));
    }

}
