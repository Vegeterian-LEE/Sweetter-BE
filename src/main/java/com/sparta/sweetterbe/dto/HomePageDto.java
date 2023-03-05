package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.Post;

import java.util.List;

public class HomePageDto {
    private List<PostResponseDto> allPostResponse;
    private List<PostResponseDto> followedPost;

    public HomePageDto(List<PostResponseDto> allPostResponse, List<PostResponseDto> followedPosts) {
        this.allPostResponse = allPostResponse;
        this.followedPost = followedPosts;
    }
}
