package com.local_dating.user_service.domain.entity;

import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class UserLoginLog {

    public UserLoginLog(final UserLoginLogVO vo) {
        this.userId = vo.userId();
        this.ip = vo.ip();
        this.lgFailYn = vo.lgFailYn();
        this.inDate = vo.localDateTime();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long no;
    //private Long id;

    @Column(name = "user_id")
    private Long userId;
    //private String userId;

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
