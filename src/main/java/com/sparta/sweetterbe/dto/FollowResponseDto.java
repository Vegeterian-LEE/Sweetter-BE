package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.Follow;

public class FollowResponseDto {
    private Long followerId;
    private boolean isAccepted;

    FollowResponseDto (Follow follow) {
        this.followerId = follow.getId();
        this.isAccepted = follow.isAccepted();;
    }

    public static FollowResponseDto of (Follow follow){
        return new FollowResponseDto(follow);
    }
}
