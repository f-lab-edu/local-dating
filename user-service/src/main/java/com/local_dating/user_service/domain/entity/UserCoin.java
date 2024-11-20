package com.local_dating.user_service.domain.entity;

import com.local_dating.user_service.domain.vo.UserCoinVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table
@NoArgsConstructor
public class UserCoin {

    public UserCoin(UserCoinVO vo) {
        this.userId = vo.userId();
        this.balance = vo.balance();
    }

    public UserCoin(String userId, Long balance) {
        this.userId = userId;
        this.balance = balance;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long seq;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "balance")
    private Long balance;

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
