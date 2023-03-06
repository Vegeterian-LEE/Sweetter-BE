package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.dto.PostResponseDto;
import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByIdOrderByCreatedAtDesc(Long id);

    List<Post> findAllByUserOrderByCreatedAtDesc(User user);

    List<Post> findAllByUserNotOrderByCreatedAtDesc(User user);

    @Query("select p from Post p" +
            " left join p.bookMarkSet m" +
            " group by p.id" +
            " order by count(m) desc")
    List<PostResponseDto> findAllByBookMarkSet();


}
