package com.sparta.sweetterbe.controller;

import com.sparta.sweetterbe.dto.*;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.service.PostService;
import com.sparta.sweetterbe.service.S3UploadService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final S3UploadService s3UploadService;


    // 다중 업로드 가능하게 List로 받습니다.
    @PostMapping(consumes = "multipart/form-data", value="/upload")
    public StatusResponseDto<List<String>> uploadImage(@RequestParam(value = "image") List<MultipartFile> multipartFiles) throws IOException {
        return StatusResponseDto.success(s3UploadService.uploadFiles(multipartFiles, "sweetter"));
    }

    //게시글 작성
    @PostMapping("/post")
    public StatusResponseDto<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto,@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return StatusResponseDto.success(postService.createPost(requestDto, userDetails));
    }

    //게시글 삭제
    @DeleteMapping("/post/{postId}")
    public StatusResponseDto<String> deletePost(@PathVariable Long postId,@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) throws AuthenticationException {
        return postService.deletePost(postId, userDetails);
    }

    //리트윗 기능
    @PostMapping("/retweet/{postId}")
    public StatusResponseDto<?> reTweetAndUnreTweet(@PathVariable Long postId,
                                                    @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.reTweetAndUnreTweet(postId, userDetails);
    }

    //메인 홈 페이지 post 조회 기능
    @GetMapping("/home")
    public StatusResponseDto<HomePageDto> getHome(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails){
        return StatusResponseDto.success(postService.getHome(userDetails));
    }

    // 북 마크 기능
    @GetMapping("/BookMarkes")
    public StatusResponseDto<List<PostResponseDto>> getPostByBookMark(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return StatusResponseDto.success(postService.getPostsByBookMark(userDetails));
    }

    //게시글 좋아요 기능
    @PostMapping("/post/like/{postId}")
    public StatusResponseDto<IsLikeResponseDto> likePost(@PathVariable Long postId,
                                                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.likePost(postId, userDetails);
    }
}
