package com.sparta.sweetterbe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequestDto {

    private String content;
    private List<String> imageUrls;
}
