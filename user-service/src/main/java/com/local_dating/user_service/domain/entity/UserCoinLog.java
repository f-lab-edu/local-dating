package com.local_dating.user_service.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table
@NoArgsConstructor
public class UserCoinLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "diff")
    private Long diff;

    @Column(name = "action")
    private String action;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private String inUser;

}
