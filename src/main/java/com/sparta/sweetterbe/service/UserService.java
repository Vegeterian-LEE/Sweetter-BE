package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.*;
import com.sparta.sweetterbe.entity.Follow;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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
    private final S3UploadService s3UploadService;

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

        User user = new User(userId, password, username, email);
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

/*    @Transactional
    public UserResponseDto updateProfile(UserRequestDto userRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        if (!userRequestDto.getNewPassword().isEmpty()){
            user.updatePassword(passwordEncoder.encode(userRequestDto.getNewPassword()));
        }
        user.update(userRequestDto);
        return new UserResponseDto(user);
    }*/
    @Transactional
    public List<UserListDto> getUserList(UserDetailsImpl userDetails) {
        List<User> users = userRepository.findAllByUserIdNot(userDetails.getUser().getUserId());
        List<UserListDto> userList = new ArrayList<>();
        for (User user : users) {
            boolean followCheck = !followRepository.findAllByFollowing_IdAndFollower_Id(userDetails.getUser().getId(), user.getId()).isEmpty();
            if (!followCheck) {
                userList.add(new UserListDto(user, false));
            }
        }
        return userList;
    }

    //이메일, 아이디, username search하는 한글자라도 같은 게 나오면 모두 조회되는 듯
    @Transactional
    public List<UserResponseDto> searchUser(String searchWord, UserDetailsImpl userDetails) {
        List<User> allUser = userRepository.findAllByUsernameLikeOrEmailLikeOrUserIdLike("%" + searchWord + "%",
                "%" + searchWord + "%", "%" + searchWord + "%");
        List<UserResponseDto> searchUserList = new ArrayList<>();
        for (User user : allUser){
            if (!user.getUserId().equals(userDetails.getUser().getUserId())){
                boolean followCheck = !followRepository.findAllByFollowing_IdAndFollower_Id(userDetails.getUser().getId(), user.getId()).isEmpty();
                searchUserList.add(new UserResponseDto(user, followCheck));
        }
        }
        return searchUserList;
    }

    @Transactional
    public UserInfoResponseDto getUserInfo(UserDetailsImpl userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저가 존재하지 않습니다")
        );
        int followernumber=0;
        int followingnumber=0;
        for(Follow follow : user.getFollowers()){
           // if(follow.isAccepted()){
                followingnumber++;
           // }
        }
        for(Follow follow : user.getFollowings()){
           // if(follow.isAccepted()){
                followernumber++;
           // }
        }
        return new UserInfoResponseDto(user,followernumber,followingnumber);
    }
    @Transactional
    public UserResponseDto setProfile(String username, String introduction, String newPassword, MultipartFile profileImage, MultipartFile backgroundImage, UserDetailsImpl userDetails) throws IOException {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        if (!newPassword.isEmpty()){
            user.updatePassword(passwordEncoder.encode(newPassword));
        }
        String profileImageUrl = s3UploadService.uploadFile(profileImage, "sweetter");
        String backgroundImageUrl = s3UploadService.uploadFile(backgroundImage, "sweetter");
        user.update(username, introduction, profileImageUrl, backgroundImageUrl);
        return new UserResponseDto(user);
    }



}
