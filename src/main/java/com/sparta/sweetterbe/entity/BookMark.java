package com.sparta.sweetterbe.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "BookMark")
@NoArgsConstructor
public class BookMark extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_ID", nullable = false)
    private Post post;


    public BookMark(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
