package com.sparta.sweetterbe.service;

import com.sparta.sweetterbe.dto.*;
import com.sparta.sweetterbe.entity.*;
import com.sparta.sweetterbe.repository.*;
import com.sparta.sweetterbe.entity.Post;
import com.sparta.sweetterbe.repository.PostLikeRepository;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.security.sasl.AuthenticationException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final RetweetRepository retweetRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final BookMarkRepository bookMarkRepository;
    @Transactional(readOnly = true)
    //각 Post에 내가 좋아요 리트윗 했는지
    public HomePageDto getHome(UserDetailsImpl userDetails) {
        User user = userRepository.findByUserId(userDetails.getUser().getUserId()).orElseThrow(
                () -> new EntityNotFoundException("회원을 찾지 못했습니다.")
        );
        List<Post> allPost = postRepository.findAllByUserNotOrderByCreatedAtDesc(user);
        List<PostResponseDto> allPostResponse = new ArrayList<>();
        for (Post post : allPost){
            boolean retweetCheck = !retweetRepository.findAllByUserIdAndPostId(user.getId(),post.getId()).isEmpty();
            boolean likeCheck = !postLikeRepository.findAllByUserIdAndPostId(user.getId(),post.getId()).isEmpty();
            allPostResponse.add(new PostResponseDto(post, retweetCheck, likeCheck));
        }
        // 팔로우 한 유저의 게시글만 조회
        List<PostResponseDto> followedPostResponse = new ArrayList<>();
        List<Follow> followList= followRepository.findAllByFollowing_IdAndIsAccepted(user.getId(),true);
        for (Post post : allPost) {
            for (int i=0; i<followList.size(); i++){
        if (followList.get(i).getFollower().getId()==post.getUser().getId()){
            boolean retweetCheck = !retweetRepository.findAllByUserIdAndPostId(user.getId(),post.getId()).isEmpty();
            boolean likeCheck = !postLikeRepository.findAllByUserIdAndPostId(user.getId(),post.getId()).isEmpty();
            followedPostResponse.add(new PostResponseDto(post, retweetCheck, likeCheck));}
        }
        }
        return new HomePageDto(allPostResponse, followedPostResponse);
    }
    @Transactional
    //게시글 생성
    public PostResponseDto createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUserId(userDetails.getUser().getUserId()).orElseThrow(
                () -> new EntityNotFoundException("회원을 찾지 못했습니다.")
        );
        
        Post post = new Post(requestDto, user);
        postRepository.save(post);
        return new PostResponseDto(post);
    }
    @Transactional
    //게시글 삭제
    public StatusResponseDto<String> deletePost(Long postId, UserDetailsImpl userDetails) throws AuthenticationException {
        User user = userDetails.getUser();
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글을 찾지 못합니다.")
        );
        if (user.getRole() == UserRoleEnum.ADMIN || user.getUserId().equals(post.getUser().getUserId())) {
            postRepository.deleteById(postId);
        } else {
            throw new AuthenticationException("작성자만 삭제가 가능합니다.");
        }
        return StatusResponseDto.success("삭제 성공");
    }

    //리트윗 기능
    @Transactional
    public StatusResponseDto<?> reTweetAndUnreTweet(Long postId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Post post = isPresentPost(postId);
        List<Retweet> retweetList = retweetRepository.findAllByUserIdAndPostId(user.getId(), postId);

        for (Retweet retweet : retweetList) {
            if (retweet.getUser().equals(user)) {
                retweetRepository.delete(retweet);
                return StatusResponseDto.success(false);
            }
        }

        Retweet retweet = Retweet.builder()
                .user(user)
                .post(post)
                .build();
        retweetRepository.save(retweet);
        return StatusResponseDto.success(true);
    }
    @Transactional
    // 게시글 좋아요 기능
    public StatusResponseDto<IsLikeResponseDto> likePost(Long id, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("게시글을 찾지 못했습니다.")
        );
        Optional<PostLike> postLike = postLikeRepository.findByPostAndUser(post, userDetails.getUser());
        if (postLike.isPresent()) {
            postLikeRepository.deleteById(postLike.get().getId());
            return StatusResponseDto.success(new IsLikeResponseDto("해당 게시글의 좋아요가 취소 되었습니다.", false));
        }
        postLikeRepository.save(new PostLike(post, userDetails.getUser()));
        return StatusResponseDto.success(new IsLikeResponseDto("해당 게시글의 좋아요가 추가 되었습니다.", true));
    }

    //해당 게시물이 존재하는지 알아 보는 경우
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost_review = postRepository.findById(id);
        return optionalPost_review.orElse(null);
    }


    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByBookMark(UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("인증된 유저가 아닙니다")
        );

        List<BookMark> bookMarks = bookMarkRepository.findByUser(user);
        bookMarks.sort(Comparator.comparing(BookMark::getModifiedAt).reversed());
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(BookMark bookMark: bookMarks){
            postResponseDtoList.add(new PostResponseDto(bookMark.getPost()));
        }
        return postResponseDtoList;
    }
    @Transactional(readOnly = true)
    public PostResponseDto getPostDetails(Long postId, UserDetailsImpl userDetails) {
        User user = userRepository.findByUserId(userDetails.getUser().getUserId()).orElseThrow(
                () -> new EntityNotFoundException("회원을 찾지 못했습니다."));
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글을 찾지 못합니다."));
        boolean retweetCheck = !retweetRepository.findAllByUserIdAndPostId(user.getId(),post.getId()).isEmpty();
        boolean postLikeCheck = !postLikeRepository.findAllByUserIdAndPostId(user.getId(),post.getId()).isEmpty();
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : comments){
            boolean commentLikeCheck = !commentLikeRepository.findAllByUserIdAndCommentId(user.getId(),comment.getId()).isEmpty();
            commentList.add(new CommentResponseDto(comment, commentLikeCheck));
        }
        PostResponseDto postDetails = new PostResponseDto(post, retweetCheck, postLikeCheck, commentList);
        return postDetails;
    }
}

