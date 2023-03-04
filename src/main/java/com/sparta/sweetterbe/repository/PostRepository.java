package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
