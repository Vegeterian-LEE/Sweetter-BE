package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByIdOrderByCreatedAtDesc(Long id);

    List<Post> findAllByUserOrderByCreatedAtDesc(User user);

    List<Post> findAll();
}
