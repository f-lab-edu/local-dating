package com.local_dating.user_service.domain.entity;

import com.local_dating.user_service.domain.vo.UserVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table
public class User {

    public User() {
    }

    public User(UserVO vo) {
        this.userid = vo.userid();
        this.pwd = vo.pwd();
        this.name = vo.name();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userid")
    private String userid;

    @Column(name = "pwd")
    private String pwd;

    @Column(name = "name")
    private String name;

    @Column(name = "birth")
    //@Column(name = "birth", length = 8)
    private String birth;

    //@Column(length = 1)
    //private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "addr")
    private String addr;

    @Column(name = "addr_dtl")
    private String addrDtl;

    @Column(name = "post_no", length = 5)
    private String postNo;

    @Column(name = "status_cd")
    private String statusCd;

    @Column(name = "match_cd")
    private String matchCd;

    @Column(name = "lg_fail")
    private Long lgFail;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Column(name = "last_pwd_mod_date")
    private LocalDateTime lastPwdModdate;

    @Column(name = "indate")
    private LocalDateTime indate;

    @Column(name = "inuser")
    private String inuser;

    @Column(name = "moddate")
    private LocalDateTime moddate;

    @Column(name = "moduser")
    private String moduser;
}
