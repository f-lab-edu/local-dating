package com.local_dating.matching_service.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class MatchingScheduleRequested {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "matching_id")
    private Long matchingId;

    @Column(name = "matching_schedule_round_id")
    private Long matchingScheduleRoundId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "matching_date")
    private LocalDate matchingDate;

    @Column(name = "matching_time_type")
    private String matchingTimeType;

    @Column(name = "status_cd") // 제출, 확정, 취소 등
    private String statusCd;

    @Column(name = "in_user")
    private Long inUser;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "mod_user")
    private Long modUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;

    public MatchingScheduleRequested(Long matchingId, Long matchingScheduleRoundId, Long userId, LocalDate matchingDate, String matchingTimeType, String statusCd) {
        this.matchingId = matchingId;
        this.matchingScheduleRoundId = matchingScheduleRoundId;
        this.userId = userId;
        this.matchingDate = matchingDate;
        this.matchingTimeType = matchingTimeType;
        this.statusCd = statusCd;
    }
}
