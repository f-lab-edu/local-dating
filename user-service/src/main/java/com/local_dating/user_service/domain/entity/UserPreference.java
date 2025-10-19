package com.local_dating.user_service.domain.entity;

import com.local_dating.user_service.domain.vo.UserPreferenceVO;
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
public class UserPreference {

    public UserPreference() {
    }

    public UserPreference(final UserPreferenceVO vo) {
        this.userId = vo.userId();
        this.prefCd = vo.prefCd();
        this.prefVal = vo.prefVal();
        this.prior = vo.prior();
        this.inUser = vo.userId();
        this.modUser = vo.userId();
    }

    public UserPreference(final Long userId, final UserPreferenceVO userPreferenceVO) {
        this.userId = userId;
        this.prefCd = userPreferenceVO.prefCd();
        this.prefVal = userPreferenceVO.prefVal();
        this.prior = userPreferenceVO.prior();
        this.inUser = userId;
        this.modUser = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "prefCd")
    private String prefCd;

    @Column(name = "prefVal")
    private String prefVal;

    @Column(name = "prior")
    private int prior;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private Long inUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private Long modUser;
}
