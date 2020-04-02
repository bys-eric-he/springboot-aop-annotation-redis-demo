package com.example.aop.redis.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -6237732230116228977L;
    /**
     * 指定主键使用自增策略 GenerationType.IDENTITY,防止自动生成hibernate_sequence表
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdateDate;
}
