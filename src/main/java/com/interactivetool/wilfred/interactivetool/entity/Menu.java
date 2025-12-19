package com.interactivetool.wilfred.interactivetool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ussd_menu")
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String menuCode;
    private String title;
    private Long parentMenuId;
    private Integer displayOrder;
    private Boolean isActive;
    private Boolean isRoot;
    private Boolean isBack;
    @Enumerated(EnumType.STRING)
    private MenuType menuType;
    private String eventId;

}
