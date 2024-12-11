package com.local_dating.consumer.domain.entity;

import com.local_dating.consumer.domain.vo.UserLoginLogVO;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
public class UserLoginLog {

    /*public UserLoginLog(final UserLoginLogVO vo) {
        this.userId = vo.getUserId();
        this.ip = vo.getIp();
        this.lgFailYn = vo.getLgFailYn();
        //this.inDate = vo.localDateTime();
    }*/

    //record 클래스 시
    public UserLoginLog(final UserLoginLogVO vo) {
        this.userId = vo.userId();
        this.ip = vo.ip();
        this.lgFailYn = vo.lgFailYn();
        this.inDate = vo.localDateTime();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "ip")
    private String ip;

    @Column(name = "lg_fail_yn")
    private String lgFailYn;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private String inUser;
}
