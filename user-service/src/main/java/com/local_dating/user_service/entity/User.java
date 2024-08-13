package com.local_dating.user_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private int id;

    @Column(nullable = false)
    private String userid;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String name;

    @Column(length = 8, nullable = false)
    //@Column(name = "birtg", length = 8, nullable = false)
    private String birth;

    //@Column(length = 1)
    //private String gender;


    private String email;

    private String phone;

    private String addr;

    @Column(name = "post_no", length = 5)
    private String postNo;

    private String status;

    @Column(name = "lg_fail")
    private Long lgFail;

    private LocalDateTime lastLoginDate;

    private LocalDateTime lastPwdModdate;

    private LocalDateTime indate;

    private LocalDateTime moddate;
}
