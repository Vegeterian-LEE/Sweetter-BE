package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomePageDto {
    private List<PostResponseDto> allPostResponse;
    private List<PostResponseDto> followedPostResponse;

    public HomePageDto(List<PostResponseDto> allPostResponse, List<PostResponseDto> followedPostResponse) {
        this.allPostResponse = allPostResponse;
        this.followedPostResponse = followedPostResponse;
    }
}
