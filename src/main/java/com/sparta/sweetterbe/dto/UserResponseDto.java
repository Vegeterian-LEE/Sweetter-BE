package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String userId;
    private String username;
    private String profileImage;
    private String backgroundImage;
    private String introduction;
    private String email;

    public UserResponseDto(User user){
        this.username = user.getUsername();
        this.userId = user.getUserId();
        this.profileImage = user.getProfileImage();
        this.backgroundImage = user.getBackgroundImage();
        this.introduction = user.getIntroduction();
        this.email = user.getEmail();
    }
}
