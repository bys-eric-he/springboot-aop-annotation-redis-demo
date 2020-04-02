package com.example.aop.redis.demo.annotation;

import java.lang.annotation.*;

/**
 * 注解 ClearCacheKey 是方法级别的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface ClearCacheKey {
    /**
     * 缓存键名称注解，默认值为类名称
     *
     * @return
     */
    String keyName() default "ClearCacheKey";
}
