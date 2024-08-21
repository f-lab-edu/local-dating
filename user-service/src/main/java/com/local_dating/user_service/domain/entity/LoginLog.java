package com.local_dating.user_service.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String userid;

    private String ip;

    //
    @Column(name = "lg_fail_yn")
    private String lgFailYn;

    private LocalDateTime indate;
}
