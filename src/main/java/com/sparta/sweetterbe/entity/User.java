package com.sparta.sweetterbe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.sweetterbe.dto.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

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
    private String userId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String username;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String profileImage;
    @Column
    private String backgroundImage;
    @Column
    private String introduction;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "follower")
    @JsonIgnore
    private List<Follow> followers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "following")
    @JsonIgnore
    private List<Follow> followings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<BookMark> bookMarkList;

    public User(String userId, String password, String username, String email, UserRoleEnum role) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public void update(UserRequestDto userRequestDto) {
        this.profileImage = userRequestDto.getProfileImage();
        this.backgroundImage = userRequestDto.getBackgroundImage();
        this.introduction = userRequestDto.getIntroduction();
        this.username = userRequestDto.getUsername();
        this.email = userRequestDto.getEmail();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}