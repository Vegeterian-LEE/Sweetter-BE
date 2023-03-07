package com.sparta.sweetterbe.repository;

import com.sparta.sweetterbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(String userId);
    List<User> findAllByUserIdNot(String userId);
    List<User> findAllByUsernameLikeOrEmailLikeOrUserIdLike(String searchWord1,String searchWord2, String searchWord3);
}
