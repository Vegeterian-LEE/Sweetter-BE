package com.sparta.sweetterbe.profile.dto;

import com.sparta.sweetterbe.dto.CommentResponseDto;
import com.sparta.sweetterbe.entity.Comment;
import com.sparta.sweetterbe.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProfileResponseDto {

    private Long id;
    private String content;
    private String userId;
    private String username;
    private List<String> imageUrls;
    //    private String imageUrl2;
//    private String imageUrl3;
//    private String imageUrl4;
    private int commentCount;
    private int retweetCount;
    private int likeCount;
    private LocalDateTime createdAt;
    private Boolean retweetCheck;
    private Boolean postLikeCheck;
    private Boolean commentCheck;
    private Boolean bookmarkCheck;

    private List<CommentResponseDto> commentList = new ArrayList<>();

    @Builder
    public ProfileResponseDto(Post post, boolean bookmarkCheck, boolean retweetCheck, boolean postLikeCheck){
        this.id = post.getId();
        this.content = post.getContent();
        this.imageUrls = post.getImageUrls();
        this.commentCount = post.getComments().size();
        this.retweetCount = post.getRetweets().size();
        this.likeCount = post.getLikes().size();
        this.retweetCheck = retweetCheck;
        this.postLikeCheck = postLikeCheck;
        this.bookmarkCheck =bookmarkCheck;
        this.userId = post.getUser().getUserId();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
    }

    @Builder
    public ProfileResponseDto(Post post, boolean bookmarkCheck,
                              boolean retweetCheck, boolean postLikeCheck,
                              Comment comment){
        this.id = post.getId();
        this.content = post.getContent();
        this.imageUrls = post.getImageUrls();
        this.commentCount = post.getComments().size();
        this.retweetCount = post.getRetweets().size();
        this.likeCount = post.getLikes().size();
        this.retweetCheck = retweetCheck;
        this.postLikeCheck = postLikeCheck;
        this.bookmarkCheck =bookmarkCheck;
        this.userId = post.getUser().getUserId();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
        this.commentCheck=true;
        this.commentList.add(new CommentResponseDto(comment));
    }

    @Builder
    public ProfileResponseDto(Post post, boolean bookmarkCheck,
                              boolean postLikeCheck){
        this.id = post.getId();
        this.content = post.getContent();
        this.imageUrls = post.getImageUrls();
        this.commentCount = post.getComments().size();
        this.retweetCount = post.getRetweets().size();
        this.likeCount = post.getLikes().size();
        this.userId = post.getUser().getUserId();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
        this.postLikeCheck = postLikeCheck;
        this.bookmarkCheck =bookmarkCheck;
    }
}
