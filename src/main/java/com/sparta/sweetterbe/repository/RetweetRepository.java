package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.Retweet;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    List<Retweet> findAllByUserOrderByCreatedAtDesc(User user);
}