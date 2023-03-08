package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.CommentRequestDto;
import com.sparta.sweetterbe.dto.CommentResponseDto;
import com.sparta.sweetterbe.dto.IsLikeResponseDto;
import com.sparta.sweetterbe.dto.StatusResponseDto;
import com.sparta.sweetterbe.entity.*;
import com.sparta.sweetterbe.repository.CommentLikeRepository;
import com.sparta.sweetterbe.repository.CommentRepository;
import com.sparta.sweetterbe.repository.PostRepository;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {


    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;


    // 댓글 작성
    public StatusResponseDto<CommentResponseDto> createComment(Long id, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Post post = postRepository.findById(id).orElseThrow(() -> new NullPointerException("등록되지 않은 게시글입니다."));
        Comment comment = new Comment(userDetails.getUser(), post, commentRequestDto.getContent());
        commentRepository.save(comment);
        return StatusResponseDto.success(new CommentResponseDto(comment));
    }


    // 댓글 삭제
    public StatusResponseDto<String> deleteComment(Long id, UserDetailsImpl userDetails) throws AuthenticationException {
        User user = userDetails.getUser();
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("댓글을 찾을 수 없습니다")
        );
        if (user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(comment.getUser().getUsername())) {
            commentRepository.deleteById(id);
            return StatusResponseDto.success("댓글을 삭제하였습니다");
        } else {
            throw new AuthenticationException("작성자만 수정이 가능합니다.");
        }
    }

    // 댓글 좋아요
    public StatusResponseDto<IsLikeResponseDto> likeComment(Long id, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글"));
        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByCommentAndUser(comment, userDetails.getUser());
        if (optionalCommentLike.isPresent()) { // 유저가 이미 좋아요를 눌렀을 때
            commentLikeRepository.deleteById(optionalCommentLike.get().getId());
            return StatusResponseDto.success(new IsLikeResponseDto("댓글에 좋아요가 취소 되었습니다.", false));
        }
        commentLikeRepository.save(new CommentLike(comment, userDetails.getUser()));
        return StatusResponseDto.success(new IsLikeResponseDto("댓글 좋아요 성공하였습니다", true));
    }
}
