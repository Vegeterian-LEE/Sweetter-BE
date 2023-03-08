package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@Setter
@Getter
public class CommentResponseDto {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCount;
    private Boolean likeCheck;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getLikes().size();
    }
    public CommentResponseDto(Comment comment, boolean likeCheck){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getLikes().size();
        this.likeCheck = likeCheck;
    }

//    public static CommentResponseDto from(Comment comment){
//        return CommentResponseDto.builder()
//                .id(comment.getId())
//                .content(comment.getContent())
//                .username(comment.getUser().getUsername())
//                .createdAt(comment.getCreatedAt())
//                .nickname(comment.getUser().getNickname())
//                .modifiedAt(comment.getModifiedAt())
//                .build();
//    }
}
