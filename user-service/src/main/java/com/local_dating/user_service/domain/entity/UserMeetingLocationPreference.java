package com.local_dating.user_service.domain.entity;

import com.local_dating.user_service.domain.vo.UserMeetingLocationPreferenceVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
//@NoArgsConstructor
@Table(name = "user_meeting_location_preference")
public class UserMeetingLocationPreference {

    public UserMeetingLocationPreference(UserMeetingLocationPreferenceVO userMeetingLocationPreferenceVO) {
        this.userId = userMeetingLocationPreferenceVO.userId();
        this.areaName = userMeetingLocationPreferenceVO.areaName();
        this.address = userMeetingLocationPreferenceVO.address();
        this.addressDetail1 = userMeetingLocationPreferenceVO.addressDetail1();
        this.addressDetail2 = userMeetingLocationPreferenceVO.addressDetail2();
        this.latitude = userMeetingLocationPreferenceVO.latitude();
        this.longitude = userMeetingLocationPreferenceVO.longitude();
        this.priority = userMeetingLocationPreferenceVO.priority();
        this.activeYn = "Y";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail1")
    private String addressDetail1;

    @Column(name = "address_detail2")
    private String addressDetail2;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "priority")
    private int priority;

    @Column(name = "active_yn")
    private String activeYn;

    @CreationTimestamp
    @Column(name = "in_date")
    private LocalDateTime inDate;

    @Column(name = "in_user")
    private Long inUser;

    @UpdateTimestamp
    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private Long modUser;
}
