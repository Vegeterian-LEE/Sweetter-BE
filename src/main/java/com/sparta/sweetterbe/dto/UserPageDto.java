package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class UserPageDto {
    private List<PostResponseDto> tweetList;
    private List<PostResponseDto> tweetAndReplyList;
    private List<PostResponseDto> mediaPostList;
    private List<PostResponseDto> likePostList;

    public UserPageDto(List<PostResponseDto> tweetList, List<PostResponseDto> tweetAndReplyList, List<PostResponseDto> mediaPostList, List<PostResponseDto> likePostList) {
        this.tweetList = tweetList;
        this.tweetAndReplyList = tweetAndReplyList;
        this.mediaPostList = mediaPostList;
        this.likePostList = likePostList;
    }
}
