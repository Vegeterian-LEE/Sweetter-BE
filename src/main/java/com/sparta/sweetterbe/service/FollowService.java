package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.FollowResponseDto;
import com.sparta.sweetterbe.entity.Follow;
import com.sparta.sweetterbe.entity.User;
import com.sparta.sweetterbe.repository.FollowRepository;
import com.sparta.sweetterbe.repository.UserRepository;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
            String followername,
            UserDetailsImpl userDetails
    ) throws AccessDeniedException{
        // 인증된 사용자 이름으로 사용자 정보를 DB에서 조회
        User following = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        // 팔로우할 유저를 ID로 조회
        User follower = userRepository.findByUsername(followername).orElseThrow(
                () -> new EntityNotFoundException("유저를 조회할 수 없습니다")
        );

        //팔로우 신청한 유저와 당한 유저는 동일할 순 없음
        if(following.getId()==follower.getId()){
            throw new AccessDeniedException("팔로우 요청이 올바르지 않습니다");
        }

        // 팔로잉(팔로우 신청한 사람), 팔로워(신청을 받은 사람)으로 팔로우 엔티티 형성
        Follow follow = followRepository.findByFollowingAndFollower(following,follower).orElse(null);
        //save로 넣을 때는 Entity로 넣어야하기에 새로 Entity를 생성
        Follow check = new Follow(follower, following);
        if(follow==null){
            followRepository.save(check);

        }

        // 팔로우 처리가 완료되었거나, 요청을 했는 데 팔로우 신청을 한 번 더한 경우
        // -> 팔로우 삭제
        else{
            throw new AccessDeniedException("팔로우를 추가할 수 없습니다");
        }
        return FollowResponseDto.of(new Follow(follower,following));
    }

    @Transactional
    public void deleteFollow(
            String followername,
            UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        // 인증된 사용자 이름으로 사용자 정보를 DB에서 조회
        User following = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        // 팔로우할 유저를 ID로 조회
        User follower = userRepository.findByUsername(followername).orElseThrow(
                () -> new EntityNotFoundException("유저를 조회할 수 없습니다")
        );

        //팔로우 신청한 유저와 당한 유저는 동일할 순 없음
        if (following.getId() == follower.getId()) {
            throw new AccessDeniedException("팔로우 요청이 올바르지 않습니다");
        }
        // 팔로잉(팔로우 신청한 사람), 팔로워(신청을 받은 사람)으로 팔로우 엔티티 형성
        Follow follow = followRepository.findByFollowingAndFollower(following, follower).orElse(null);
        //save로 넣을 때는 Entity로 넣어야하기에 새로 Entity를 생성

        if (follow.getFollower() != follower) {
            throw new AccessDeniedException("팔로우 삭제 요청이 올바르지 않습니다");
        }
        followRepository.delete(follow);
    }


    @Transactional
    public FollowResponseDto approveFollow(String followingname, UserDetailsImpl userDetails)throws AccessDeniedException{
        User follower = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () ->  new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        // 팔로우받을 유저를 ID로 조회
        User following = userRepository.findByUsername(followingname).orElseThrow(
                () -> new EntityNotFoundException("유저를 조회할 수 없습니다")
        );

        //팔로우 신청한 유저와 당한 유저는 동일할 순 없음
        if (following.getId() == follower.getId()) {
            throw new AccessDeniedException("팔로우 요청이 올바르지 않습니다");
        }

        //팔로우 신청한 사람, 팔로우 신청당한 사람 모두 관계를 고려하여 DB 조회
        Follow follow = followRepository.findByFollowingAndFollower(following,follower).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저의 팔로우 신청을 확인할 수 없습니다")
        );
        follow.approve();
        return FollowResponseDto.of(follow);
    }

    @Transactional
    public void denyFollow(String followingname, UserDetailsImpl userDetails) throws AccessDeniedException {
        User follower = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        // 팔로우할 유저를 ID로 조회
        User following = userRepository.findByUsername(followingname).orElseThrow(
                () -> new EntityNotFoundException("유저를 조회할 수 없습니다")
        );

        //팔로우 신청한 유저와 당한 유저는 동일할 순 없음
        if (following.getId() == follower.getId()) {
            throw new AccessDeniedException("팔로우 요청이 올바르지 않습니다");
        }

        //팔로우 신청한 사람, 팔로우 신청당한 사람 모두 관계를 고려하여 DB 조회
        Follow follow = followRepository.findByFollowingAndFollower(following,follower).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저의 팔로우 신청을 확인할 수 없습니다")
        );
        followRepository.delete(follow);
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
