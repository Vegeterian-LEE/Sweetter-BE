package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.BookMark;
import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    Optional<BookMark> findById(Long id);
    List<BookMark> findByUser(User user);
    Optional<BookMark> findByIdAndUserId(Long BookmarkId,Long userId);
    Optional<BookMark> findByUserAndPost(User user, Post post);
}
