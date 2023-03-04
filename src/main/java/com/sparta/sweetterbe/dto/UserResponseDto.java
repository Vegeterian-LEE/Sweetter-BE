package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String userId;
    private String username;
    private String image;
    private String introduction;
    private String email;

    public UserResponseDto(User user){
        this.username = user.getUsername();
        this.userId = user.getUserId();
        this.image = user.getImage();
        this.introduction = user.getIntroduction();
        this.email = user.getEmail();
    }
}
