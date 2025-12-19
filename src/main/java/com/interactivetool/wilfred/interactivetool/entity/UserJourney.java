package com.interactivetool.wilfred.interactivetool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_journey")
public class UserJourney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    String imsi;
    String msdn;
    String serviceCode;
    String inputText;
    String responseText;
}
