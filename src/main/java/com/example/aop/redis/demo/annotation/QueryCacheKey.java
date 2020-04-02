package com.example.aop.redis.demo.annotation;

import java.lang.annotation.*;

/**
 * 注解 QueryCacheKey 是方法级别的注解，用来标注要查询数据的主键，会和上面的nameSpace组合做缓存的key值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface QueryCacheKey {
    /**
     * 缓存键名称注解，默认值为类名称
     *
     * @return
     */
    String keyName() default "QueryCacheKey";
}
