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
@Table(name = "code_group")
public class CodeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_group")
    private String codeGroup;

    @Column(name = "description")
    private String description;

    @Column(name = "use_yn")
    private String useYn;

    @Column(name = "in_date")
    @CreationTimestamp
    private java.time.LocalDateTime inDate;

    @Column(name = "in_user")
    private String inUser;

    @Column(name = "mod_date")
    @UpdateTimestamp
    private LocalDateTime modDate;

    @Column(name = "mod_user")
    private String modUser;
}
