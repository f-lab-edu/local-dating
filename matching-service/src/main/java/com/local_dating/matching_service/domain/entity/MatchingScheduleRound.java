package com.local_dating.matching_service.domain.entity;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
public class MatchingScheduleRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "matching_id")
    private Long matchingId;

    @Column(name = "round")
    private Integer round;

    @Column(name = "status_cd") // 제출중, 양쪽제출, 교집합없음(라운드종료) 등
    private String statusCd;

    @Column(name = "round_start_date")
    private LocalDate roundStartDate;

    @Column(name = "round_end_date")
    private LocalDate roundEndDate;

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

    public MatchingScheduleRound(final Long matchingId, final Integer round, final String statusCd, final LocalDate roundStartDate, final LocalDate roundEndDate) {
        this.matchingId = matchingId;
        this.statusCd = statusCd;
        this.roundStartDate = roundStartDate;
        this.roundEndDate = roundEndDate;
    }
}
