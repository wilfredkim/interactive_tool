package com.interactivetool.wilfred.interactivetool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ussd_session")
public class Session extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    private String msisdn;
    private String imsi;
    private Long currentMenuId;
    private Boolean isActive;
    private String ussdString;
    private String currentInput;
    private String navigationPath;
    private Integer pinAttempts;
}
