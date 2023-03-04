package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.PostLike;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(PostLike postLike, User user);
}
