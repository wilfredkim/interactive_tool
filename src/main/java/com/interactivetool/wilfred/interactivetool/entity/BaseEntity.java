package com.interactivetool.wilfred.interactivetool.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @CreatedDate
    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_last_updated")
    @LastModifiedDate
    public Instant dateLastUpdated;
}
