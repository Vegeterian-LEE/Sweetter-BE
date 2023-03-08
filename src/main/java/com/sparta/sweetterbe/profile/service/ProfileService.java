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

    // 첫번째 리스트
    @Transactional
    public List<ProfileResponseDto> getTweetList(Long userId, UserDetailsImpl userDetails) {

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾지 못했습니다.")
        );

        List<Post> postList = postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        boolean retweetCheck_tmp=false;
        List<ProfileResponseDto> tweetList = new ArrayList<>();
        // tweet된 게시글을 보여줄 때 retweetcheck는 null로 보여서 수정
        // ProfileResponseDto에서 retweetcheck가 변수로 선언되었는 데
        // 오버로드 외에 값을 안 넣어주면 null이 생겨서 수정
        for(Post post : postList){
            for(Retweet retweet : post.getRetweets()){
                Boolean retweetCheck = retweet.getUser().getId().equals(user.getId());
                retweetCheck_tmp=retweetCheck;
            }
            tweetList.add(new ProfileResponseDto(post,retweetCheck_tmp));
        }

        List<Retweet> retweetList = retweetRepository.findAllByUserId(user.getId());
        for(int i = 0; i < retweetList.size(); i++){
            Boolean retweetCheck = retweetList.get(i).getUser().getId().equals(user.getId());
            /*if(retweetCheck == null){
                retweetCheck = false;
            }*/
            tweetList.add(new ProfileResponseDto(retweetList.get(i).getPost(), retweetCheck));
        }
        tweetList = DeduplicationUtils.deduplication(tweetList, ProfileResponseDto::getId);

        //o2-o1으로 해야 id 별로 내림차순 정렬
        Collections.sort(tweetList, ((o1, o2) -> (int)(o2.getId() - o1.getId())));
        return tweetList;
    }

    //두번째 리스트
    @Transactional
    public List<ProfileResponseDto> getUserList(Long userId, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾을 수 없습니다.")
        );

        List<Post> postList = postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        boolean retweetCheck_Intweet=false;
        List<ProfileResponseDto> tweetList = new ArrayList<>();
        // tweet된 게시글을 보여줄 때 retweetcheck는 null로 보여서 수정
        // ProfileResponseDto에서 retweetcheck가 변수로 선언되었는 데
        // 오버로드 외에 값을 안 넣어주면 null이 생겨서 수정
        for(Post post : postList){
            for(Retweet retweet : post.getRetweets()){
                Boolean retweetCheck = retweet.getUser().getId().equals(user.getId());
                retweetCheck_Intweet=retweetCheck;
            }
            tweetList.add(new ProfileResponseDto(post,retweetCheck_Intweet));
        }

        //리트윗한 게시글도 참조
        List<Retweet> retweetList = retweetRepository.findAllByUserId(user.getId());
        for(int i = 0; i < retweetList.size(); i++){
            Boolean retweetCheck = retweetList.get(i).getUser().getId().equals(user.getId());
            /*if(retweetCheck == null){
                retweetCheck = false;
            }*/
            tweetList.add(new ProfileResponseDto(retweetList.get(i).getPost(), retweetCheck));
        }
        tweetList = DeduplicationUtils.deduplication(tweetList, ProfileResponseDto::getId);

        //댓글 단 트윗도 조회
        List<Comment> commentlist = commentRepository.findAllByUserOrderByCreatedAtDesc(user);
        boolean retweetCheck_Inretweet=false;

        for(Comment comment: commentlist){
            for(Retweet retweet : comment.getPost().getRetweets()){
                Boolean retweetCheck = retweet.getUser().getId().equals(user.getId());
                retweetCheck_Inretweet=retweetCheck;
            }
            tweetList.add(new ProfileResponseDto(comment.getPost(), retweetCheck_Inretweet));
        }
        tweetList = DeduplicationUtils.deduplication(tweetList, ProfileResponseDto::getId);

        /*List<Comment> comments = commentRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<CommentResponseDto> commentList = new ArrayList<>();
        for(Comment comment : comments){
            commentList.add(new CommentResponseDto(comment));
        }*/
        //return new ProfileSecondListResponsDto(tweetList, commentList);
        //o2-o1으로 해야 내림차순 정렬
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
                MediaPostList.add(new ProfileResponseDto(post));
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
        List<PostLike> likeList = postLikeRepository.findAllByUser(user);
        List<ProfileResponseDto> likePostList = new ArrayList<>();
        for (int i = 0; i < likeList.size(); i++){
            likePostList.add(new ProfileResponseDto(likeList.get(i).getPost()));
        }

        Collections.sort(likePostList, (o1, o2) -> (int)(o2.getId() - o1.getId()));
        return likePostList;
    }
}
