package com.local_dating.user_service.domain.entity;

import com.local_dating.user_service.domain.vo.UserVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table
public class User {
    public User() {
    }

    public User(final UserVO vo) {
        this.no = vo.no();
        //this.id = vo.id();
        //this.userId = vo.userId();
        this.loginId = vo.loginId();
        this.pwd = vo.pwd();
        this.name = vo.name();
        this.nickname = vo.nickname();
        this.birth = vo.birth();
        this.phone = vo.phone();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long no;
    //private Long id;

    @Column(name = "login_id")
    private String loginId;
    /*@Column(name = "user_id")
    private String userId;*/

    @Column(name = "pwd")
    private String pwd;

    @Column(name = "name")
    private String name;
    
    @Column(name = "nickname")
    private String nickname; //추가

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
    private LocalDateTime lastPwdModDate;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private String inUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private String modUser;

    //@OneToMany(mappedBy = "a", fetch = FetchType.LAZY)
    //private List<UserRecomCard> userRecomCards;
}