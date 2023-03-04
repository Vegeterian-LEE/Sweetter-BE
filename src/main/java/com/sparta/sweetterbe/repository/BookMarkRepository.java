package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.BookMark;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    Optional<BookMark> findById(Long id);
    Optional<BookMark> findByUserAndBookMark(User user, BookMark bookMark);
}