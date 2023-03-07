package com.sparta.sweetterbe.repository;


import com.sparta.sweetterbe.entity.Comment;
import com.sparta.sweetterbe.entity.CommentLike;
import com.sparta.sweetterbe.entity.PostLike;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

    List<CommentLike> findAllByUserIdAndCommentId(Long userId, Long commentId);
}
