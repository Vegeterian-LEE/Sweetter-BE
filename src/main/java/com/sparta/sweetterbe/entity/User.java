package com.sparta.sweetterbe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.sweetterbe.dto.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
    @Column(nullable = false, unique = true)
    private String username;
    @Email // @가 없거나 영문이 아닌 한글인 경우, 특수기호는 오류
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
    private UserRoleEnum role = UserRoleEnum.USER;

    @OneToMany(mappedBy = "user",orphanRemoval = true, cascade=CascadeType.REMOVE)
    @OrderBy("createdAt DESC")
    private Set<Post> postSet = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "follower")
    @JsonIgnore
    private List<Follow> followers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "following")
    @JsonIgnore
    private List<Follow> followings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<BookMark> bookMarkList;

    public User(String userId, String password, String username, String email) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.email = email;
    }

    public void update(UserRequestDto userRequestDto) {
        this.profileImage = userRequestDto.getProfileImage();
        this.backgroundImage = userRequestDto.getBackgroundImage();
        this.introduction = userRequestDto.getIntroduction();
        this.username = userRequestDto.getUsername();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}