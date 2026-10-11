package com.local_dating.user_service.domain.entity;

import com.local_dating.user_service.domain.vo.RecommendationShowedVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_showed")
@Getter
@RequiredArgsConstructor
public class RecommendationShowed {

    public RecommendationShowed(RecommendationShowedVO recommendationShowedVO) {
        this.viewerUserId = recommendationShowedVO.viewerUserId();
        this.targetUserId = recommendationShowedVO.targetUserId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "viewer_user_id")
    private Long viewerUserId;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private Long inUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private Long modUser;

}
