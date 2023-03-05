package com.sparta.sweetterbe.dto;


import com.sparta.sweetterbe.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FollowRequestDto {
    private User user;
    private boolean isAccepted;
}
