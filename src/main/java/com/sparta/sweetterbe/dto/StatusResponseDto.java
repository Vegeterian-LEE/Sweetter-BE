package com.sparta.sweetterbe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusResponseDto<T> {

    private int statusCode;
    private String message;
    private T data;

    public StatusResponseDto(int statusCode, String message, T data){
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> StatusResponseDto<T> success(T data){
        return new StatusResponseDto<>(200, "success", data);
    }

    public static <T> StatusResponseDto<T> fail(int statusCode, T data){
        return new StatusResponseDto<>(statusCode, "fail", data);
    }

}
