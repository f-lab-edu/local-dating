package com.local_dating.user_service.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_policy")
@Getter
@Setter
public class PricingPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "target_group", length = 20)
    private String targetGroup;

    @Column(name = "target_cd", length = 20)
    private String targetCd;

    @Column(name = "target_name", length = 100)
    private String targetName;

    @Column(name = "action_type", length = 20)
    private String actionType;

    @Column(name = "price")
    private Long price;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "in_user")
    private Long inUser;

    @Column(name = "in_date")
    @CreationTimestamp
    private LocalDateTime inDate;

    @Column(name = "mod_user")
    private Long modUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;
}
