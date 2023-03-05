package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookMarkRequestDto {
    private Post post;
    private User user;
}
