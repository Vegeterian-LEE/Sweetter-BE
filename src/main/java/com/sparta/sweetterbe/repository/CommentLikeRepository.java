package com.sparta.sweetterbe.repository;


import com.sparta.sweetterbe.entity.Comment;
import com.sparta.sweetterbe.entity.CommentLike;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
}
