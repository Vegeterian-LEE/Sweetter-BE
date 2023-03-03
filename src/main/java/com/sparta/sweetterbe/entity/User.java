package com.sparta.sweetterbe.entity;

import com.sparta.sweetterbe.dto.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String image;
    @Column
    private String introduction;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(String username, String password, String nickName, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.nickname = nickName;
        this.email = email;
        this.role = role;
    }

    public void update(UserRequestDto userRequestDto) {
        this.image = userRequestDto.getImage();
        this.introduction = userRequestDto.getIntroduction();
        this.nickname = userRequestDto.getNickname();
        this.email = userRequestDto.getEmail();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}