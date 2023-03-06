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

    public HomePageDto getHome(UserDetailsImpl userDetails) {
        List<Post> allPost= postRepository.findAll();
        List<PostResponseDto> allPostResponse = new ArrayList<>();
        for (Post post : allPost){
        allPostResponse.add(new PostResponseDto(post));
        }
        List<PostResponseDto> followedPosts = new ArrayList<>();
        for (Post post : allPost) {
            for (int i=0; i<userDetails.getUser().getFollowings().size(); i++)
        if (post.getUser().equals(userDetails.getUser().getFollowings().get(i))){
            followedPosts.add(new PostResponseDto(post));
        }
        }
        return new HomePageDto(allPostResponse, followedPosts);
    }

    //지금 다 유저로 찾고 있는데 id값(숫자)로 찾아야하는것 아닐지 고민좀..
    public UserPageDto getUserPage(UserDetailsImpl userDetails) {
        User user = userRepository.findByUserId(userDetails.getUser().getUserId()).orElseThrow(
                () -> new EntityNotFoundException("회원을 찾지 못했습니다.")
        );
        // 각 포스트에 댓글 갯수 / 좋아요 갯수 / 리트윗 갯수 붙어서 나가야함
        //작성글 + 리트윗 글
        List<Retweet> retweetList = retweetRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<Post> postList = postRepository.findAllByUserOrderByCreatedAtDesc(user);// 엔티티 모양이 달라서 같은 모양으로 맞춰줘야함.
        List<PostResponseDto> tweetList = new ArrayList<>();
        for (int i = 0; i< retweetList.size(); i++){
            tweetList.add(new PostResponseDto(retweetList.get(i).getPost(), retweetList.get(i)));
        }
        for (int i = 0; i < postList.size(); i++) {
            tweetList.add(new PostResponseDto(postList.get(i)));
        }
        // 중복 삭제처리
        tweetList.sort(Comparator.comparing(LocalDateTime::new));
        tweetList = tweetList.stream().distinct().collect(Collectors.toList());
//        Set<PostResponseDto> responseDtoSet = new HashSet<>(tweetLists);
//        List<PostResponseDto> tweetList = new ArrayList<>(responseDtoSet);// 다 합쳐서 시간순 정렬 해야하는데 아직 얘는 잘 모르겠음
        //post가 아니라 댓글 갯수 / 좋아요 갯수 / 리트윗 갯수가 붙은 dto로 반환해야함

        //작성글, 리트윗글 내가 댓글 단 글까지
        List<Comment> commentList = commentRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<PostResponseDto> tweetAndReplyList = tweetList;
        for (int i = 0; i < commentList.size(); i++) {
            tweetAndReplyList.add(new PostResponseDto(commentList.get(i).getPost(), commentList.get(i)));
        }
        // 중복 삭제처리 필요
        tweetAndReplyList.sort(Comparator.comparing(LocalDateTime::new));
        tweetAndReplyList = tweetAndReplyList.stream().distinct().collect(Collectors.toList());// 다 합쳐서 시간순 정렬 해야하는데 아직 얘는 잘 모르겠음

        //media가 있는 게시글
        List<Post> MediaList = postRepository.findAllByIdOrderByCreatedAtDesc(user.getId());
        List<PostResponseDto> MediaPostList = new ArrayList<>();
        for (int i = 0; i < MediaList.size(); i++){
            if(MediaList.get(i).getImageUrls() == null){
                MediaList.remove(i);
            }else {
                MediaPostList.add(new PostResponseDto(MediaList.get(i)));
            }
        }
        MediaPostList.sort(Comparator.comparing(LocalDateTime::new));
        //이미지 없는걸로 선별 걸어서 다시
        
        //like한 게시글
        List<PostLike> likeList = postLikeRepository.findAllByUser(user);
        List<PostResponseDto> likePostList = new ArrayList<>();
        for (int i = 0; i < likeList.size(); i++){
            likePostList.add(new PostResponseDto(likeList.get(i).getPost()));
        }

        return new UserPageDto(tweetList, tweetAndReplyList, MediaPostList, likePostList);
    }

    //게시글 생성
    public PostResponseDto createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = new Post(requestDto, userDetails.getUser());
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //게시글 삭제
    public StatusResponseDto<String> deletePost(Long postId, UserDetailsImpl userDetails) throws AuthenticationException {
        User user = userDetails.getUser();
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("해당 게시글을 찾지 못합니다.")
        );

        if (user.getRole() == UserRoleEnum.ADMIN || user.getUserId().equals(post.getUser().getUsername())) {
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

    @Transactional
    public List<PostResponseDto> getPostsByQueryCondition() {
        return postRepository.findAllByBookMarkSet();
    }

}
