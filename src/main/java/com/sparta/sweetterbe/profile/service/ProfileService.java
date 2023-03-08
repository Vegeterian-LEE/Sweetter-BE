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
        //o2-o1으로 해야 내림차순 정렬
        Collections.sort(tweetList, ((o1, o2) -> (int)(o2.getId() - o1.getId())));
        tweetList = DeduplicationUtils.deduplication(tweetList, ProfileResponseDto::getId);

        return tweetList;
    }

    //두번째 리스트
    @Transactional
    public ProfileSecondListResponsDto getUserList(Long userId, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾을 수 없습니다.")
        );

        List<Post> postList = postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        List<ProfileResponseDto> tweetList = new ArrayList<>();
        for(Post post : postList){
            tweetList.add(new ProfileResponseDto(post));
        }

        List<Retweet> retweetList = retweetRepository.findAllByUserId(user.getId());
        for(int i = 0; i < retweetList.size(); i++){
            Boolean retweetCheck = retweetList.get(i).getUser().getId().equals(user.getId());
            if(retweetCheck == null){
                retweetCheck = false;
            }
            tweetList.add(new ProfileResponseDto(retweetList.get(i).getPost(), retweetCheck));
        }
        Collections.sort(tweetList, ((o1, o2) -> (int)(o1.getId() - o2.getId())));
        tweetList = DeduplicationUtils.deduplication(tweetList, ProfileResponseDto::getId);

        List<Comment> comments = commentRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<CommentResponseDto> commentList = new ArrayList<>();
        for(Comment comment : comments){
            commentList.add(new CommentResponseDto(comment));
        }
        return new ProfileSecondListResponsDto(tweetList, commentList);
    }

    @Transactional
    public List<ProfileResponseDto> getMediaList(Long userId, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾지 못했습니다.")
        );

        List<Post> MediaList = postRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<ProfileResponseDto> MediaPostList = new ArrayList<>();
        for (int i = 0; i < MediaList.size(); i++){
            if(MediaList.get(i).getImageUrls().isEmpty()){
                MediaList.remove(i);
            }else {
                MediaPostList.add(new ProfileResponseDto(MediaList.get(i)));
            }
        }
        Collections.sort(MediaPostList, (o1, o2) -> (int)(o1.getId() - o2.getId()));

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
        return likePostList;
    }
}
