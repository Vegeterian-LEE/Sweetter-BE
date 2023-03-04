package com.sparta.sweetterbe.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IsLikeResponseDto {

    private String result;
    private boolean isLike;

    public IsLikeResponseDto(String result, boolean isLike){
        this.result = result;
        this.isLike = isLike;
    }


}
