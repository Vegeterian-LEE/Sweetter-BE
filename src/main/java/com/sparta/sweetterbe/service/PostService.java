package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.IsLikeResponseDto;
import com.sparta.sweetterbe.dto.StatusResponseDto;
import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.entity.PostLike;
import com.sparta.sweetterbe.repository.PostLikeRepository;
import com.sparta.sweetterbe.repository.PostRepository;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;


    public StatusResponseDto<IsLikeResponseDto> likePost(Long id, UserDetailsImpl userDetails){
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("게시글을 찾지 못했습니다.")
        );
        Optional<PostLike> postLike = postLikeRepository.findByPostAndUser(post, userDetails.getUser());
        if (postLike.isPresent()){
            postLikeRepository.deleteById(postLike.get().getId());
            return StatusResponseDto.success(new IsLikeResponseDto("해당 게시글의 좋아요가 취소 되었습니다.", false));
        }
        postLikeRepository.save(new PostLike(post, userDetails.getUser()));
        return StatusResponseDto.success(new IsLikeResponseDto("해당 게시글의 좋아요가 추가 되었습니다.", true));
    }
}
