package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.BookMarkResponseDto;
import com.sparta.sweetterbe.entity.BookMark;
import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.entity.User;
import com.sparta.sweetterbe.repository.BookMarkRepository;
import com.sparta.sweetterbe.repository.PostRepository;
import com.sparta.sweetterbe.repository.UserRepository;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMarkService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;

    @Transactional
    public BookMarkResponseDto toggleMark(
            Long postId,
            UserDetailsImpl userDetails
    ) {
        // 인증된 사용자 이름으로 사용자 정보를 DB에서 조회
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        // 입력된 게시글 정보를 DB에서 조회
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("게시글이 없습니다")
        );

        //Optional 형식이지만 뒤에 .orElse(null); 추가하면 <>안의 타입으로 바뀜
        BookMark bookMark = bookMarkRepository.findByUserAndPost(user,post).orElse(null);
        if (bookMark == null) {
            bookMarkRepository.save(new BookMark(user,post));
            return BookMarkResponseDto.of(true);
        }
        else {
            bookMarkRepository.delete(bookMark);
            return BookMarkResponseDto.of(false);
        }
    }

}
