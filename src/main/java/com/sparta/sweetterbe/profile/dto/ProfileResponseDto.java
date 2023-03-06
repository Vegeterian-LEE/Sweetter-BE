package com.sparta.sweetterbe.profile.dto;

import com.sparta.sweetterbe.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Builder
    public ProfileResponseDto(Post post, boolean retweetCheck){
        this.id = post.getId();
        this.content = post.getContent();
        this.imageUrls = post.getImageUrls();
        this.commentCount = post.getComments().size();
        this.retweetCount = post.getRetweets().size();
        this.likeCount = post.getLikes().size();
        this.retweetCheck = retweetCheck;
        this.userId = post.getUser().getUserId();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
    }

    @Builder
    public ProfileResponseDto(Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.imageUrls = post.getImageUrls();
        this.commentCount = post.getComments().size();
        this.retweetCount = post.getRetweets().size();
        this.likeCount = post.getLikes().size();
        this.userId = post.getUser().getUserId();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
    }
}
