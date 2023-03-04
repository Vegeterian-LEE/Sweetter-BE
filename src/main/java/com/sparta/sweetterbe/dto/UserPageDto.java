package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class UserPageDto {
    private List<Post> tweetList;
    private List<Post> tweetAndReplyList;
    private List<Post> mediaPostList;
    private List<Post> likePostList;

    public UserPageDto(List<Post> tweetList, List<Post> tweetAndReplyList, List<Post> mediaPostList, List<Post> likePostList) {
        this.tweetList = tweetList;
        this.tweetAndReplyList = tweetAndReplyList;
        this.mediaPostList = mediaPostList;
        this.likePostList = likePostList;
    }
}
