package com.example.aop.redis.demo.convert;

public interface DTOEntityConvert<S, T> {
    /**
     * 将entity转换成dto
     * @param s
     * @return
     */
    T convertToDto(S s);

    /**
     * 将dto转换成entity
     * @param t
     * @return
     */
    S convertToEntity(T t);
}