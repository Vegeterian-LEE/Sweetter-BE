package com.sparta.sweetterbe.entity;

import com.sparta.sweetterbe.dto.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "post")
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@SQLDelete(sql = "UPDATE post SET deleted = true WHERE id = ?")
// delete 구문 입력 시 추가로 delete 컬럼이 true로 변경되도록 쿼리를 날린다.
//@Where(clause = "deleted = false")
// isDeleted의 디폴트 값은 false이다.
public class Post extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
    //entity 안에서 Arraylist나 String[] 자료형으로 저장 가능할까요 > 아니면 image1 ~ image4로 저장해야하는데... - 정환
    @Column(name = "imageList")
    @ElementCollection(targetClass = String.class)
    @Size(min = 0, max = 4)
    private List<String> imageUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //순서는 없지만 중복제거 가능
    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade=CascadeType.REMOVE)
    Set<BookMark> bookMarkSet = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<PostLike> likes = new ArrayList<>();
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<Retweet> retweets = new ArrayList<>();

    @Builder
    public Post(PostRequestDto requestDto, User user){
        this.content = requestDto.getContent();
        this.imageUrls = requestDto.getImageUrls();
        this.user = user;
    }
}