package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.entity.PostLike;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findAllByUser(User user);
    List<PostLike> findByPostAndUser(Post post, User user);
}