package com.sparta.sweetterbe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "Follow")
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean isAccepted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User following;


    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public void approve() {
        this.isAccepted = true;
    }
}