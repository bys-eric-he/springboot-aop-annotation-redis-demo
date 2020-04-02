package com.example.aop.redis.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
public class User extends BaseEntity {
    @Column(length = 15, nullable = false)
    private String name;
    @Column(length = 30)
    private String email;
    @Column(length = 11)
    private String mobile;
    @Column(length = 255)
    private String address;
}
