package com.example.aop.redis.demo.redis.config;

import lombok.Getter;

@Getter
public enum RedisConstants {
    /**
     * 数据库1
     */
    DataBase1("database1", 0),
    /**
     * 数据库2
     */
    DataBase2("database2", 1);

    RedisConstants(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private int value;

}
