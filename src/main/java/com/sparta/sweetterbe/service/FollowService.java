package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.FollowResponseDto;
import com.sparta.sweetterbe.entity.Follow;
import com.sparta.sweetterbe.entity.User;
import com.sparta.sweetterbe.repository.FollowRepository;
import com.sparta.sweetterbe.repository.UserRepository;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public FollowResponseDto makeFollow(
            Long followerId,
            UserDetailsImpl userDetails
    ) {
        // 인증된 사용자 이름으로 사용자 정보를 DB에서 조회
        User following = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        // 팔로우할 유저를 ID로 조회
        User follower = userRepository.findById(followerId).orElseThrow(
                () -> new UsernameNotFoundException("유저를 조회할 수 없습니다")
        );

        List<Follow> followList = followRepository.findAllByFollowingAndFollower(following, follower);

        // follow list에서 이미 following이 되어있다면 follow 취소로 구현
        for (Follow follow : followList) {
            if (follow.getFollowing().equals(following)) {
                followRepository.delete(follow);
                return FollowResponseDto.of(new Follow(follower,following));
            }
        }
        followRepository.save(new Follow(follower, following));
        return FollowResponseDto.of(new Follow(follower,following));
    }

    @Transactional
    public FollowResponseDto approveFollow(Long followerId, UserDetailsImpl userDetails) throws AccessDeniedException {
        User following = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () ->  new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        // 팔로우할 유저를 ID로 조회
        User follower = userRepository.findById(followerId).orElseThrow(
                () -> new UsernameNotFoundException("유저를 조회할 수 없습니다")
        );

        //팔로잉한 유저와 팔로우한 유저는 동일할 순 없음
        if (following.getId() == follower.getId()) {
            throw new AccessDeniedException("팔로우 요청이 올바르지 않습니다");
        }

        List<Follow> followList = followRepository.findAllByFollowingAndFollower(following, follower);

        // follow list에서 DB에서 조회해온 follower와 일치하는 지 확인
        // follwer이므로 승인 가능하게 해줌
        for (Follow follow : followList) {
            if (follow.getFollower().equals(follower)) {
                follow.approve();
                return FollowResponseDto.of(new Follow(follower,following));
            }
        }
        return FollowResponseDto.of(new Follow(follower,following));
    }
    @Transactional
    public void denyFollow(Long followerId, UserDetailsImpl userDetails) throws AccessDeniedException {
        User following = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        // 팔로우할 유저를 ID로 조회
        User follower = userRepository.findById(followerId).orElseThrow(
                () -> new UsernameNotFoundException("유저를 조회할 수 없습니다")
        );

        //팔로잉한 유저와 팔로우한 유저는 동일할 순 없음
        if (following.getId() == follower.getId()) {
            throw new AccessDeniedException("팔로우 요청이 올바르지 않습니다");
        }

        List<Follow> followList = followRepository.findAllByFollowingAndFollower(following, follower);

        // follow list에서 DB에서 조회해온 follower와 일치하는 지 확인
        // follwer이므로 follewer 신청 거절가능
        for (Follow follow : followList) {
            if (follow.getFollower().equals(follower)) {
                followRepository.delete(follow);
            }
        }

    }
    /*@Transactional
    public FollowResponseDto getFollowers(
            Long followerId,
            UserDetailsImpl userDetails
    ) {
        User following = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        // 팔로우할 유저를 ID로 조회
        User follower = userRepository.findById(followerId).orElseThrow(
                () -> new UsernameNotFoundException("유저를 조회할 수 없습니다")
        );

        List<Follow> followerList = followRepository.findAllByFollower(follower);


    }*/



}
