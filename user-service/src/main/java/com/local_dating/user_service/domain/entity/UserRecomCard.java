package com.local_dating.user_service.domain.entity;

import com.local_dating.user_service.domain.vo.UserRecomCardVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table
@Entity
@NoArgsConstructor
public class UserRecomCard {

    public UserRecomCard(final UserRecomCardVO vo) {
        this.userId = vo.userId();
        this.cardId = vo.cardId();
        this.useYn = vo.useYn();
        this.inUser = vo.userId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "card_id")
    private String cardId;

    @Column(name = "use_yn")
    private String useYn;
    //private String useyn;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private String inUser;

    @OneToMany(mappedBy = "userRecomCard", fetch = FetchType.LAZY)
    private List<UserProfile> userProfiles = new ArrayList<>();
}
