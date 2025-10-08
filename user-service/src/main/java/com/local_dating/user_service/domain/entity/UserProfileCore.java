package com.local_dating.user_service.domain.entity;

import com.local_dating.user_service.domain.vo.UserProfileCoreVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_profile_core")
@NoArgsConstructor
public class UserProfileCore {

    public UserProfileCore(UserProfileCoreVO vo) {
        this.userId = vo.userId();
        this.gender = vo.gender();
        this.birth = vo.birth();
        this.height = vo.height();
        this.education = vo.education();
        this.salary = vo.salary();
        this.region = vo.region();
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth")
    private Short birth;

    @Column(name = "height")
    private Short height;

    @Column(name = "education")
    private String education;

    @Column(name = "salary")
    private Short salary;

    @Column(name = "region")
    private String region;

    @Column(name = "in_date")
    @CreationTimestamp
    private java.time.LocalDateTime inDate;

    @Column(name = "in_user")
    private Long inUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private Long modUser;
}
