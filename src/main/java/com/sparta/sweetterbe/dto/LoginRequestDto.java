package com.sparta.sweetterbe.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class LoginRequestDto {
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
}
