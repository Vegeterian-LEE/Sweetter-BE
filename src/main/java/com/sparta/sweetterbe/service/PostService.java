package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.*;
import com.sparta.sweetterbe.entity.*;
import com.sparta.sweetterbe.repository.*;
import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.repository.PostLikeRepository;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.security.sasl.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final RetweetRepository retweetRepository;
    private final CommentRepository commentRepository;


    //지금 다 유저로 찾고 있는데 id값(숫자)로 찾아야하는것 아닐지 고민좀..
    public Object getUserPage(String userId, UserDetailsImpl userDetails) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                ()-> new EntityNotFoundException("회원을 찾지 못했습니다.")
        );
        //작성글 + 리트윗 글
        List<Retweet> retweetList = retweetRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<Post> postList = postRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<Post> tweetList = postList;
        for (int i=0; i < retweetList.size(); i++){
            tweetList.add(retweetList.get(i).getPost());
        }
        // 중복 삭제처리 필요
        tweetList.sort(Comparator.comparing(LocalDateTime::new)); // 다 합쳐서 시간순 정렬 해야하는데 아직 얘는 잘 모르겠음

        //작성글, 리트윗글 내가 댓글 단 글까지
        List<Comment> commentList = commentRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<Post> tweetAndReplyList = tweetList;
        for (int i=0; i < commentList.size(); i++){
            tweetAndReplyList.add(commentList.get(i).getPost());
        }
        // 중복 삭제처리 필요
        tweetAndReplyList.sort(Comparator.comparing(LocalDateTime::new)); // 다 합쳐서 시간순 정렬 해야하는데 아직 얘는 잘 모르겠음

        //media가 있는 게시글
        List<Post> MediaPostList = postRepository.findAllByIdOrderByCreatedAtDesc(user.getId()); //이미지 없는걸로 선별 걸어서 다시

        //like한 게시글
        List<PostLike> likeList = postLikeRepository.findAllByUser(user);
        List<Post> likePostList = new ArrayList<>();
        for (int i=0; i < likeList.size(); i++){
            likePostList.add(likeList.get(likeList.size()-1-i).getPost()); // 역순으로 집어넣어줘야 가장 최근에 좋아요 누른게 제일 위로
        }
        return new UserPageDto(tweetList, tweetAndReplyList, MediaPostList, likePostList);
    }

    //게시글 생성
    public StatusResponseDto<PostResponseDto> createPost(PostRequestDto requestDto, UserDetailsImpl userDetails){
        Post post = new Post(requestDto, userDetails.getUser());
        postRepository.save(post);
        return StatusResponseDto.success(new PostResponseDto(post));
    }

    //게시글 삭제
    public StatusResponseDto<String> deletePost(Long postId, UserDetailsImpl userDetails) throws AuthenticationException {
        User user = userDetails.getUser();
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new EntityNotFoundException("해당 게시글을 찾지 못합니다.")
        );

        if (user.getRole() == UserRoleEnum.ADMIN || user.getUserId().equals(post.getUser().getUsername())){
            postRepository.deleteById(postId);
        }else {
            throw  new AuthenticationException("작성자만 삭제가 가능합니다.");
        }
        return StatusResponseDto.success("삭제 성공");
    }

    //리트윗 기능


/*    public StatusResponseDto<IsLikeResponseDto> likePost(Long id, UserDetailsImpl userDetails){
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
    }*/
}
