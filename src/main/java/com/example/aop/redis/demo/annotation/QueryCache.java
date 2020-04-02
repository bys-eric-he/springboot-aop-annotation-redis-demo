package com.example.aop.redis.demo.annotation;

import com.example.aop.redis.demo.common.CacheNameSpace;

import java.lang.annotation.*;

/**
 * 注解 QueryCache 用来标识查询数据库的方法，参数nameSpace用来区分应用的，后面会用来添加到缓存的key中。
 * 比如，用户数据缓存的数据key值全部都是USER。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface QueryCache {
    CacheNameSpace nameSpace();
}
