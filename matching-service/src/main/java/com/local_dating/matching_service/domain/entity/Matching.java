package com.local_dating.matching_service.domain.entity;

import com.local_dating.matching_service.domain.vo.MatchingVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class Matching {

    public Matching(MatchingVO vo) {
        //this.id = vo.id();
        this.requId = vo.requId();
        this.recvId = vo.recvId();
        this.statusCd = vo.statusCd();
        this.requStatusCd = vo.requStatusCd();
        this.recvStatusCd = vo.recvStatusCd();
        this.matchPlace = vo.matchPlace();
        this.matchDate = vo.matchDate();
        this.matchTime = vo.matchTime();
        this.modUser = vo.requId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "requ_id")
    private long requId; // 요청자

    @Column(name = "recv_id")
    private long recvId; // 수신자

    @Column(name = "status_cd")
    private String statusCd;

    @Column(name = "requ_status_cd")
    private String requStatusCd;

    @Column(name = "recv_status_cd")
    private String recvStatusCd;

    @Column(name = "match_place")
    private String matchPlace;

    @Column(name = "match_date", length = 8)
    private String matchDate;

    @Column(name = "match_time")
    private String matchTime;

    @Column(name = "in_user")
    private long inUser;

    @Column(name = "in_date")
    private LocalDateTime inDate;

    @Column(name = "mod_user")
    private long modUser;

    @Column(name = "mod_date")
    private LocalDateTime modDate;
}
