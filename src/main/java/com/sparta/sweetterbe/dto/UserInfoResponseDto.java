package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoResponseDto {
    private String userId;
    private String username;
    private String profileImage;
    private String backgroundImage;
    private String introduction;
    private String email;
    private int followingnumber;
    private int followernumber;


    public UserInfoResponseDto(User user, int followernumber, int followingnumber){
        this.username = user.getUsername();
        this.userId = user.getUserId();
        this.profileImage = user.getProfileImage();
        this.backgroundImage = user.getBackgroundImage();
        this.introduction = user.getIntroduction();
        this.email = user.getEmail();
        this.followingnumber = followingnumber;
        this.followernumber = followernumber;
    }

}