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
    
    //지금 다 유저로 찾고 있는데 id값(숫자)로 찾아야하는것 아닐지 고민좀..
//    public UserPageDto getUserPage(Long userId, UserDetailsImpl userDetails) {
//        // 다 합쳐서 시간순 정렬 해야하는데 아직 얘는 잘 모르겠음
//        //post가 아니라 댓글 갯수 / 좋아요 갯수 / 리트윗 갯수가 붙은 dto로 반환해야함
//
//        //작성글, 리트윗글 내가 댓글 단 글까지
//        List<Comment> commentList = commentRepository.findAllByUserOrderByCreatedAtDesc(user);
//        List<PostResponseDto> tweetAndReplyList = tweetList;
//        for (int i = 0; i < commentList.size(); i++) {
//            tweetAndReplyList.add(new PostResponseDto(commentList.get(i).getPost()));
//        }
//        // 중복 삭제처리 필요
//        Collections.sort(tweetAndReplyList, ((o1, o2) -> (int)(o1.getId() - o2.getId())));
//        tweetAndReplyList= DeduplicationUtils.deduplication(tweetAndReplyList, PostResponseDto::getId);
//
//        //media가 있는 게시글
//        List<Post> MediaList = postRepository.findAllByUserOrderByCreatedAtDesc(user);
//        List<PostResponseDto> MediaPostList = new ArrayList<>();
//        for (int i = 0; i < MediaList.size(); i++){
//            if(MediaList.get(i).getImageUrls().isEmpty()){
//                MediaList.remove(i);
//            }else {
//                MediaPostList.add(new PostResponseDto(MediaList.get(i)));
//            }
//        }
//        MediaPostList.sort(Comparator.comparing(LocalDateTime::new));
//        //이미지 없는걸로 선별 걸어서 다시
//
//        //like한 게시글
//        List<PostLike> likeList = postLikeRepository.findAllByUser(user);
//        List<PostResponseDto> likePostList = new ArrayList<>();
//        for (int i = 0; i < likeList.size(); i++){
//            likePostList.add(new PostResponseDto(likeList.get(i).getPost()));
//        }
//
//        return new UserPageDto(tweetList, tweetAndReplyList, MediaPostList, likePostList);
//    }

    // 첫번째 리스트
    public List<ProfileResponseDto> getTweetList(Long userId, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾지 못했습니다.")
        );

        List<Post> postList = postRepository.findAllByIdOrderByCreatedAtDesc(user.getId());
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

        return tweetList;
    }

    //두번째 리스트
    public ProfileSecondListResponsDto getUserList(Long userId, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("해당 유저를 찾을 수 없습니다.")
        );

        List<Post> postList = postRepository.findAllByIdOrderByCreatedAtDesc(user.getId());
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
