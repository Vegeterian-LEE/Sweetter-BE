package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.entity.PostLike;
import com.sparta.sweetterbe.entity.Retweet;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {


    List<PostLike> findAllByUser(User user);
//    Optional<PostLike> findByPostAndUser(Post post, User user);
    Optional<PostLike> findByPostAndUser(Post post, User user);
    List<PostLike> findAllByUserIdAndPostId(Long userId, Long postId);
}
