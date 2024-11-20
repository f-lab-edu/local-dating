package com.local_dating.user_service.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.local_dating.user_service.domain.vo.UserProfileVO;
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
public class UserProfile {

    public UserProfile() {}

    public UserProfile(final UserProfileVO vo) {
        this.userId = vo.userId();
        this.infoCd = vo.infoCd();
        this.infoVal = vo.infoVal();
    }

    public UserProfile(final String userId, final UserProfileVO vo) {
        this.userId = userId;
        this.infoCd = vo.infoCd();
        this.infoVal = vo.infoVal();
    }

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
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private String inUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private String modUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "card_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    @JsonIgnore
    private UserRecomCard userRecomCard;
}
