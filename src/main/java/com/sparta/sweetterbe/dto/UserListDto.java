package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.User;
import com.sparta.sweetterbe.repository.FollowRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserListDto {
    private String UserId;
    private String username;
    private String introduction;
    private String image;
    private boolean followed;
    public UserListDto(User user, boolean followed){
        this.UserId = user.getUserId();
        this.username = user.getUsername();
        this.introduction = user.getIntroduction();
        this.image = user.getImage();
        this.followed = followed;
    }

}
