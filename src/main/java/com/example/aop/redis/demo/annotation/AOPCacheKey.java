package com.example.aop.redis.demo.annotation;

import com.example.aop.redis.demo.common.CacheNameSpace;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 注解 AOPCacheKey 是方法级别的注解，用来标注要查询数据的主键，会nameSpace组合做缓存的key值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AOPCacheKey {
    /**
     * 缓存名字空间，默认为空
     *
     * @return
     */
    CacheNameSpace nameSpace();

    /**
     * 缓存键名称注解，默认值为类名称
     *
     * @return
     */
    String key() default "AOPCacheKey";

    /**
     * 缓存过期时间，默认30分钟
     *
     * @return
     */
    int expireTime() default 30;

    /**
     * 缓存过期时间单位，默认为分钟
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

}
