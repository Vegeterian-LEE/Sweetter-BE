package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.*;
import com.sparta.sweetterbe.entity.User;
import com.sparta.sweetterbe.entity.UserRoleEnum;
import com.sparta.sweetterbe.jwt.JwtUtil;
import com.sparta.sweetterbe.repository.UserRepository;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
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
            throw new IllegalArgumentException("중복된 유저 네임 존재");
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
        if (!userRequestDto.getNewPassword().isEmpty() && userRequestDto.getNewPassword().equals(userRequestDto.getNewPasswordConfirm())){
            user.updatePassword(passwordEncoder.encode(userRequestDto.getNewPassword()));
        }
        else if (!userRequestDto.getNewPassword().equals(userRequestDto.getNewPasswordConfirm())) {
            throw new IllegalArgumentException("변경하려는 비밀 번호가 일치하지 않습니다.");
        }
        user.update(userRequestDto);
        return new UserResponseDto(user);
    }

    @Transactional
    public String checkPassword(PasswordRequestDto passwordRequestDto, UserDetailsImpl userDetail) {
        User user = userRepository.findById(userDetail.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        if (!passwordEncoder.matches(passwordRequestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀 번호가 틀렸습니다.");
        }
        return "비밀번호가 일치합니다.";
    }
}
