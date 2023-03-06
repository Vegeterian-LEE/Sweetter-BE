package com.sparta.sweetterbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class UserRequestDto {
    private String profileImage;
    private String backgroundImage;
    private String introduction;
    @Pattern(regexp = "(?=.*?[a-zA-Z])(?=.*?[\\d])(?=.*?[~!@#$%^&*()_+=\\-`]).{8,15}")
    private String newPassword;
    @NotNull
    private String username;

}
