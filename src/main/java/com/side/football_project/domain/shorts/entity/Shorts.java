package com.side.football_project.domain.shorts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shorts extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String url;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 생성자
     */
    @Builder
    public Shorts(String title, String description, String url, User user) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.user = user;
    }

    public void updateShorts(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
