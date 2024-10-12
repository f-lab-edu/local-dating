package com.local_dating.user_service.domain.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
public class UserLoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "ip")
    private String ip;

    @Column(name = "lg_fail_yn")
    private String lgFailYn;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private String inUser;
}
