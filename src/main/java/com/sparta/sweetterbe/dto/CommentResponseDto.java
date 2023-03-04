package com.sparta.sweetterbe.dto;

import com.sparta.sweetterbe.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;
    private String username;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
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
