package com.sparta.sweetterbe.dto;

public class BookMarkResponseDto {
    private boolean isWished;

    BookMarkResponseDto(boolean isWished){
        this.isWished = isWished;
    }

    public static BookMarkResponseDto of(boolean isWished){
        return new BookMarkResponseDto(isWished);
    }
}
