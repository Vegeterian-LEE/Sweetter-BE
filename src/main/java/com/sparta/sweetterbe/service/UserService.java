package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.*;
import com.sparta.sweetterbe.entity.User;
import com.sparta.sweetterbe.entity.UserRoleEnum;
import com.sparta.sweetterbe.jwt.JwtUtil;
import com.sparta.sweetterbe.repository.UserRepository;
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
    public StatusResponseDto signup(@Valid SignupRequestDto signupRequestDto){
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();
        String email = signupRequestDto.getEmail();

        Optional<User> foundUsername = userRepository.findByUsername(username);
        if (foundUsername.isPresent()){
            throw new IllegalArgumentException("중복된 사용자 존재");
        }
        Optional<User> foundEmail = userRepository.findByEmail(email);
        if (foundEmail.isPresent()){
            throw new IllegalArgumentException("중복된 이메일 존재");
        }
        Optional<User> foundNickname = userRepository.findByNickname(nickname);
        if (foundNickname.isPresent()){
            throw new IllegalArgumentException("중복된 닉네임 존재");
        }
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(username, password, nickname, email, role);
        userRepository.save(user);
        return  StatusResponseDto.success("가입 성공!") ;
    }

    @Transactional(readOnly = true)
    public UserResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다")
        );

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("비밀 번호가 틀렸습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateProfile(UserRequestDto userRequestDto, User user) {
        User master = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        if (!userRequestDto.getNewPassword().isEmpty() && userRequestDto.getNewPassword().equals(userRequestDto.getNewPasswordConfirm())){
            master.updatePassword(passwordEncoder.encode(userRequestDto.getNewPassword()));
        }
        else if (!userRequestDto.getNewPassword().equals(userRequestDto.getNewPasswordConfirm())) {
            throw new IllegalArgumentException("변경하려는 비밀 번호가 일치하지 않습니다.");
        }
        master.update(userRequestDto);
        return new UserResponseDto(master);
    }

    @Transactional
    public boolean checkPassword(PasswordRequestDto passwordRequestDto, User user) {
        User master = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        if (!passwordEncoder.matches(passwordRequestDto.getPassword(), master.getPassword())){
            throw new IllegalArgumentException("비밀 번호가 틀렸습니다.");
        }
        return true;
    }
}
