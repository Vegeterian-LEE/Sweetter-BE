package com.sparta.sweetterbe.profile.dto;

import com.sparta.sweetterbe.dto.CommentResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileSecondListResponsDto {

    private List<ProfileResponseDto> tweetList;
    private List<CommentResponseDto> commentList;

    public ProfileSecondListResponsDto(List<ProfileResponseDto> tweetList, List<CommentResponseDto> commentList){
        this.tweetList = tweetList;
        this.commentList = commentList;
    }
}
