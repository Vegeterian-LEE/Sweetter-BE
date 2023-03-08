package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.Follow;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findById(Long id);
    Optional<Follow> findByFollowingAndFollower(User following, User follower);
    Optional<Follow> findByFollower(User follower);

    List<Follow> findAllByFollowing(User following);


  //List<Follow> findAllByFollowing_IdAndIsAccepted(Long id, boolean Is_Accepted);
  //List<Follow> findAllByFollowing_IdAndFollower_IdAndIsAccepted(Long following_id, Long follower_id, boolean IsAccepted);
    List<Follow> findAllByFollowing_IdAndFollower_Id(Long following_id, Long follower_id);

}
