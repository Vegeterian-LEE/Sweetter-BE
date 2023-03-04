package com.sparta.sweetterbe.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
    //entity 안에서 Arraylist나 String[] 자료형으로 저장 가능할까요 > 아니면 image1 ~ image4로 저장해야하는데... - 정환
    @Column
    private String image1;
    @Column
    private String image2;
    @Column
    private String image3;
    @Column
    private String image4;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //순서는 없지만 중복제거 가능
    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade=CascadeType.REMOVE)
    Set<BookMark> bookMarkSet = new HashSet<>();


    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<PostLike> likes = new ArrayList<>();
}