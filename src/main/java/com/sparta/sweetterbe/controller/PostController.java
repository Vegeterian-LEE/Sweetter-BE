package com.sparta.sweetterbe.controller;

import com.sparta.sweetterbe.dto.IsLikeResponseDto;
import com.sparta.sweetterbe.dto.PostRequestDto;
import com.sparta.sweetterbe.dto.PostResponseDto;
import com.sparta.sweetterbe.dto.StatusResponseDto;
import com.sparta.sweetterbe.dto.UserPageDto;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.service.PostService;
import com.sparta.sweetterbe.service.S3UploadService;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/upload")
    public StatusResponseDto<List<String>> uploadImage(@RequestParam(value = "image") List<MultipartFile> multipartFiles) throws IOException {
        return StatusResponseDto.success(s3UploadService.uploadFiles(multipartFiles, "sweetter"));
    }

    /*@GetMapping("/{userId}")
    public StatusResponseDto<List<UserPageDto>> getUserPage(@PathVariable String userId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return StatusResponseDto.success(PostService.getUserPage(userId, userDetails));
    }*/


    //게시글 작성
    @PostMapping("/post")
    public StatusResponseDto<PostResponseDto> createPost(PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails);
    }

    //게시글 삭제
    @DeleteMapping("/post/{postId}")
    public StatusResponseDto<String> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws AuthenticationException {
        return postService.deletePost(postId, userDetails);
    }

    //리트윗 기능
    @PostMapping("/retweet/{postId}")
    public StatusResponseDto<?> reTweetAndUnreTweet(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.reTweetAndUnreTweet(postId, userDetails);
    }

/*    @GetMapping("/userlist")
    public StatusResponseDto<List<UserListDto>> getUserList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return StatusResponseDto.success(PostService.getUserList(userDetails));
    }*/



    //게시글 좋아요 기능
//    @PostMapping("/post/like/{postId}")
//    public StatusResponseDto<IsLikeResponseDto> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return postService.likePost(postId, userDetails);
//    }
}
