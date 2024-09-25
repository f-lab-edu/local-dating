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

    public UserPreference(final UserPreferenceVO userPreferenceVO) {
        this.userId = userPreferenceVO.userId();
        this.prefCd = userPreferenceVO.prefCd();
        this.prefVal = userPreferenceVO.prefVal();
        this.prior = userPreferenceVO.prior();
    }

    public UserPreference(final String userId, final UserPreferenceVO userPreferenceVO) {
        this.userId = userId;
        this.prefCd = userPreferenceVO.prefCd();
        this.prefVal = userPreferenceVO.prefVal();
        this.prior = userPreferenceVO.prior();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

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
    private String inUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private String modUser;
}
