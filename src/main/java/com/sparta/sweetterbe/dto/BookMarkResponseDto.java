package com.sparta.sweetterbe.dto;


import com.sparta.sweetterbe.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookMarkResponseDto {
    private boolean isMarked;

    BookMarkResponseDto(boolean isMarked){
        this.isMarked = isMarked;
    }

    public static BookMarkResponseDto of(boolean isWished){
        return new BookMarkResponseDto(isWished);
    }
}
