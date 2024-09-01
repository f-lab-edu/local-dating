package com.local_dating.user_service.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "info_cd")
    private String infoCd;

    @Column(name = "info_val")
    private String infoVal;

    @Column(name = "in_date")
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private String inUser;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private String modUser;
}
