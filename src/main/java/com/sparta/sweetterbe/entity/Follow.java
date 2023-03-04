package com.sparta.sweetterbe.entity;

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
    private User user;

    public Follow(User user) {
        this.user = user;
    }

    public void approve() {
        this.isAccepted = true;
    }
}