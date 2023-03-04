package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.Follow;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findById(Long id);
    List<Follow> findAllById(Long userId);
    Optional<Follow> findByIdAndUserId(Long followId, Long userId);
}
