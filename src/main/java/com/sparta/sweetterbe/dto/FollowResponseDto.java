package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.Follow;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FollowResponseDto {
    private String followername;
    private boolean isAccepted;

    FollowResponseDto (Follow follow) {
        this.followername = follow.getFollower().getUsername();
        this.isAccepted = follow.isAccepted();;
    }

    public static FollowResponseDto of (Follow follow){
        return new FollowResponseDto(follow);
    }
}
