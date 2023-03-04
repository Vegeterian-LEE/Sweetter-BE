package com.sparta.sweetterbe.controller;

import com.sparta.sweetterbe.service.PostService;
import com.sparta.sweetterbe.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final S3UploadService s3UploadService;


    // 다중 업로드 가능하게 List로 받습니다.
    @PostMapping(value="/upload")
    public List<String> uploadImage (@RequestParam(value = "image") List<MultipartFile> multipartFiles) throws IOException {
        return s3UploadService.uploadFiles(multipartFiles, "sweetter");
    }

}
