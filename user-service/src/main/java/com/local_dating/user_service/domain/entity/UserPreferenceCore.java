package com.local_dating.user_service.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_preference_core")
public class UserPreferenceCore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_min")
    private Short birthMin;

    @Column(name = "birth_max")
    private Short birthMax;

    @Column(name = "height_min")
    private Short heightMin;

    @Column(name = "height_max")
    private Short heightMax;

    @Column(name = "education")
    private String education;

    @Column(name = "salary_min")
    private Short salaryMin;

    @Column(name = "salary_max")
    private Short salaryMax;

    @Column(name = "range_max")
    private Short rangeMax;

    @Column(name = "in_date")
    @CreationTimestamp
    private java.time.LocalDateTime inDate;

    @Column(name = "in_user")
    private Long inUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private Long modUser;
}
