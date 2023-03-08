package com.sparta.sweetterbe.profile.service;

import com.sparta.sweetterbe.dto.CommentResponseDto;
import com.sparta.sweetterbe.dto.PostResponseDto;
import com.sparta.sweetterbe.profile.dto.ProfileResponseDto;
import com.sparta.sweetterbe.dto.UserPageDto;
import com.sparta.sweetterbe.entity.*;
import com.sparta.sweetterbe.profile.dto.ProfileSecondListResponsDto;
import com.sparta.sweetterbe.repository.*;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.service.DeduplicationUtils;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final RetweetRepository retweetRepository;
    private final CommentRepository commentRepository;
    private final BookMarkRepository bookMarkRepository;

    // 첫번째 리스트
    @Transactional
    public List<ProfileResponseDto> getTweetList(Long userId, UserDetailsImpl userDetails) {

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾지 못했습니다.")
        );

        List<Post> postList = postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        List<ProfileResponseDto> tweetList = new ArrayList<>();
        for(Post post : postList){
            Boolean postLikeCheck = !postLikeRepository.findAllByUserIdAndPostId(user.getId(),post.getId()).isEmpty();
            Boolean bookMarkCheck = !bookMarkRepository.findAllByUserIdAndPostId(user.getId(), post.getId()).isEmpty();
            tweetList.add(new ProfileResponseDto(post,bookMarkCheck,postLikeCheck));
        }

        List<Retweet> retweetList = retweetRepository.findAllByUserId(user.getId());
        for(Retweet retweet: retweetList){
            Boolean bookMarkCheck = !bookMarkRepository.findAllByUserIdAndPostId(user.getId(),
                    retweet.getPost().getId()).isEmpty();
            Boolean postLikeCheck = !postLikeRepository.findAllByUserIdAndPostId(user.getId(),
                    retweet.getPost().getId()).isEmpty();
            Boolean retweetCheck = retweet.getUser().getId().equals(user.getId());
            tweetList.add(new ProfileResponseDto(retweet.getPost(), bookMarkCheck, retweetCheck,postLikeCheck));
        }
        tweetList = DeduplicationUtils.deduplication(tweetList, ProfileResponseDto::getId);
        Collections.sort(tweetList, ((o1, o2) -> (int)(o2.getId() - o1.getId())));
        return tweetList;
    }

    //두번째 리스트
    @Transactional
    public List<ProfileResponseDto> getUserList(Long userId, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾지 못했습니다.")
        );

        List<Post> postList = postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        List<ProfileResponseDto> tweetList = new ArrayList<>();
        for(Post post : postList){
            Boolean bookMarkCheck = !bookMarkRepository.findAllByUserIdAndPostId(user.getId(),
                    post.getId()).isEmpty();
            Boolean postLikeCheck = !postLikeRepository.findAllByUserIdAndPostId(user.getId(),post.getId()).isEmpty();
            tweetList.add(new ProfileResponseDto(post,bookMarkCheck,postLikeCheck));
        }

        List<Retweet> retweetList = retweetRepository.findAllByUserId(user.getId());
        for(Retweet retweet: retweetList){
            Boolean bookMarkCheck = !bookMarkRepository.findAllByUserIdAndPostId(user.getId(),
                    retweet.getPost().getId()).isEmpty();
            Boolean postLikeCheck = !postLikeRepository.findAllByUserIdAndPostId(user.getId(),
                    retweet.getPost().getId()).isEmpty();
            Boolean retweetCheck = retweet.getUser().getId().equals(user.getId());
            tweetList.add(new ProfileResponseDto(retweet.getPost(), bookMarkCheck,retweetCheck,postLikeCheck));
        }

        //댓글 단 트윗도 조회
        List<Comment> commentlist = commentRepository.findAllByUserOrderByCreatedAtDesc(user);
        for(Comment comment: commentlist){
            Boolean bookMarkCheck = !bookMarkRepository.findAllByUserIdAndPostId(user.getId(),
                    comment.getPost().getId()).isEmpty();
            Boolean postLikeCheck = !postLikeRepository.findAllByUserIdAndPostId(user.getId(),
                    comment.getPost().getId()).isEmpty();
            Boolean retweetCheck = !retweetRepository.findAllByUserIdAndPostId(user.getId(),
                    comment.getPost().getId()).isEmpty();
            tweetList.add(new ProfileResponseDto(comment.getPost(),bookMarkCheck, retweetCheck,postLikeCheck,comment));
        }
        tweetList = DeduplicationUtils.deduplication(tweetList, ProfileResponseDto::getId);

        //o2-o1으로 해서 내림차순 정렬
        Collections.sort(tweetList, ((o1, o2) -> (int)(o2.getId() - o1.getId())));
        return tweetList;
    }

    @Transactional
    public List<ProfileResponseDto> getMediaList(Long userId, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾지 못했습니다.")
        );

        List<Post> postList = postRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<ProfileResponseDto> MediaPostList = new ArrayList<>();
        /*for (int i = 0; i < postList.size(); i++){
            if(postList.get(i).getImageUrls().isEmpty()){
                postList.remove(i);
            }else {
                MediaPostList.add(new ProfileResponseDto(postList.get(i)));
            }
        }*/
        for(Post post: postList){
            if(!post.getImageUrls().isEmpty()){
                Boolean bookMarkCheck = !bookMarkRepository.findAllByUserIdAndPostId(user.getId(),
                        post.getId()).isEmpty();
                Boolean postLikeCheck = !postLikeRepository.findAllByUserIdAndPostId(user.getId(),
                        post.getId()).isEmpty();
                Boolean retweetCheck = !retweetRepository.findAllByUserIdAndPostId(user.getId(),
                        post.getId()).isEmpty();
                MediaPostList.add(new ProfileResponseDto(post,bookMarkCheck,retweetCheck,postLikeCheck));
            }
        }

        //생성된 순으로 정렬한 것에 imageUrls의 값이 있는 post만 낚아채서 add할꺼라
        //다시 정렬하지 않아도 그대로 나옵니다
        //Collections.sort(MediaPostList, ((o1, o2) -> (int)(o2.getId() - o1.getId())));
        return MediaPostList;
    }
    @Transactional
    public List<ProfileResponseDto> getLikeList(Long userId, UserDetailsImpl userDetails){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾을 수 없습니다.")
        );
        List<PostLike> likeList = postLikeRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<ProfileResponseDto> likePostList = new ArrayList<>();
        for(PostLike postLike: likeList){
            Boolean bookMarkCheck = !bookMarkRepository.findAllByUserIdAndPostId(user.getId(),
                    postLike.getPost().getId()).isEmpty();
            Boolean postLikeCheck = postLike.getUser().getId().equals(user.getId());
            Boolean retweetCheck = !retweetRepository.findAllByUserIdAndPostId(user.getId(),
                    postLike.getPost().getId()).isEmpty();

            likePostList.add(new ProfileResponseDto(postLike.getPost(),bookMarkCheck,retweetCheck,postLikeCheck));
        }
        return likePostList;
    }
}
