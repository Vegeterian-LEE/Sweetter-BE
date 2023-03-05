package com.sparta.sweetterbe.dto;


import com.sparta.sweetterbe.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookMarkResponseDto {
    private boolean isWished;

    BookMarkResponseDto(boolean isWished){
        this.isWished = isWished;
    }

    public static BookMarkResponseDto of(boolean isWished){
        return new BookMarkResponseDto(isWished);
    }
}
