package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.Retweet;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    Optional<Retweet> findAllByUserOrderByCreatedAtDesc(User user);
    List<Retweet> findAllByUserIdAndPostId(Long userId, Long postId);
}
