package com.local_dating.matching_service.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class MatchingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "matching_id")
    private Long matchingId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "matching_date")
    private LocalDate matchingDate;

    @Column(name = "matching_time")
    private LocalTime matchingTime;

    @Column(name = "in_user")
    private long inUser;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "mod_user")
    private long modUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;
}
