package com.local_dating.consumer.domain.entity;

import com.local_dating.consumer.domain.vo.UserCoinLogVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table
@NoArgsConstructor
public class UserCoinLog {

    public UserCoinLog(final UserCoinLogVO vo) {
        this.userId = vo.userId();
        this.action = vo.action();
        this.diff = vo.diff();
        this.inDate = vo.localDateTime();
        this.inUser = vo.inUserId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "diff")
    private Long diff;

    @Column(name = "action")
    private String action;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private String inUser;

}
