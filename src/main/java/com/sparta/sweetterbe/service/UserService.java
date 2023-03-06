package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.*;
import com.sparta.sweetterbe.entity.User;
import com.sparta.sweetterbe.entity.UserRoleEnum;
import com.sparta.sweetterbe.jwt.JwtUtil;
import com.sparta.sweetterbe.repository.FollowRepository;
import com.sparta.sweetterbe.repository.UserRepository;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String signup(@Valid SignupRequestDto signupRequestDto){
        String userId = signupRequestDto.getUserId();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String username = signupRequestDto.getUsername();
        String email = signupRequestDto.getEmail();

        Optional<User> foundUserId = userRepository.findByUserId(userId);
        if (foundUserId.isPresent()){
            throw new IllegalArgumentException("중복된 아이디 존재");
        }
        Optional<User> foundEmail = userRepository.findByEmail(email);
        if (foundEmail.isPresent()){
            throw new IllegalArgumentException("중복된 이메일 존재");
        }
        Optional<User> foundUsername = userRepository.findByUsername(username);
        if (foundUsername.isPresent()){
            throw new IllegalArgumentException("중복된 유저네임 존재");
        }

        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(userId, password, username, email, role);
        userRepository.save(user);
        return  "회원 가입 성공!";
    }

    @Transactional(readOnly = true)
    public UserResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다")
        );

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("비밀 번호가 틀렸습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateProfile(UserRequestDto userRequestDto, UserDetailsImpl userDetail) {
        User user = userRepository.findById(userDetail.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        if (!userRequestDto.getNewPassword().isEmpty()){
            user.updatePassword(passwordEncoder.encode(userRequestDto.getNewPassword()));
        }
        user.update(userRequestDto);
        return new UserResponseDto(user);
    }
    @Transactional
    public List<UserListDto> getUserList(UserDetailsImpl userDetails) {
        List<User> users = userRepository.findAllByUserIdNot(userDetails.getUser().getUserId());
        List<UserListDto> userList = new ArrayList<>();
        for (int i=0; i< users.size(); i++){
            boolean followed = !followRepository.findAllByFollowing_IdAndFollower_IdAndIsAccepted(userDetails.getUser().getId(), users.get(i).getId(), true).isEmpty();
            userList.add(new UserListDto(users.get(i), followed));
        }
        return userList;
    }
}
