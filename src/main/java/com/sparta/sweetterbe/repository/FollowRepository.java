package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.Follow;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findById(Long id);
    List<Follow> findAllById(Long userId);
    List<Follow> findAllByFollowingAndFollower(User following, User follower);
    Optional<Follow> findByFollowingAndFollower(User following, User follower);
    Optional<Follow> findByFollower(User follower);
    List<Follow> findAllByFollower(User follower);
    List<Follow> findAllByFollowing(User following);

    int countAllByFollower(User follower);
    int countAllByFollowing(User following);

}
