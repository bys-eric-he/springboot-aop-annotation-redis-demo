package com.example.aop.redis.demo.aspect;

import com.example.aop.redis.demo.annotation.QueryCache;
import com.example.aop.redis.demo.annotation.QueryCacheKey;
import com.example.aop.redis.demo.common.CacheNameSpace;
import com.example.aop.redis.demo.redis.RedisUtil;
import com.example.aop.redis.demo.redis.config.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 查询缓存切面
 */
@Aspect
@Service
@Slf4j
@Order(1)
public class QueryCacheAspect {

    private RedisUtil redisUtil;

    @Autowired
    public QueryCacheAspect(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 定义拦截规则：拦截所有@QueryCache注解的方法。
     */
    /*@Pointcut("execution(* com.example.aop.redis.demo.service.impl..*(..)) , @annotation(com.example.aop.redis.demo.annotation.QueryCache)")
    public void queryCachePointcut(){}*/
    @Pointcut("@annotation(com.example.aop.redis.demo.annotation.QueryCache)")
    public void queryCachePointcut() {
    }

    /**
     * 拦截器具体实现
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("queryCachePointcut()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        log.info("AOP 缓存切面处理 >>>> start ");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取被拦截的方法
        Method method = signature.getMethod();
        //获取方法注解
        CacheNameSpace cacheType = method.getAnnotation(QueryCache.class).nameSpace();
        String key = null;
        int i = 0;

        // 循环所有的参数
        for (Object value : pjp.getArgs()) {
            MethodParameter methodParam = new SynthesizingMethodParameter(method, i);
            //获取参数注解
            Annotation[] parameterAnnotations = methodParam.getParameterAnnotations();

            // 循环参数上所有的注解
            for (Annotation paramAnn : parameterAnnotations) {
                if (paramAnn instanceof QueryCacheKey) {
                    QueryCacheKey queryCacheKey = (QueryCacheKey) paramAnn;
                    //取到QueryCacheKey的标识参数的值
                    key = cacheType + "_" + queryCacheKey.keyName() + "_" + value;
                    break;
                }
            }
            i++;
        }

        //获取不到key值，抛异常
        if (StringUtils.isBlank(key)) throw new Exception("缓存key值不存在");

        log.info("获取到缓存key值 >>>> " + key);
        boolean hasKey = redisUtil.hasKey(key);
        if (hasKey) {

            // 缓存中获取到数据，直接返回。
            Object object = redisUtil.get(key, RedisConstants.DataBase1.getValue());
            log.info("从缓存中获取到数据 >>>> " + object.toString());
            log.info("AOP 缓存切面处理 >>>> end 耗时：" + (System.currentTimeMillis() - beginTime));
            return object;
        }

        //缓存中没有数据，调用原始方法查询数据库
        Object object = pjp.proceed();
        //设置超时时间30分钟
        redisUtil.set(key, object, RedisConstants.DataBase1.getValue(), 5);

        log.info("DB取到数据并存入缓存 >>>> " + object.toString());
        log.info("AOP 缓存切面处理 >>>> end 耗时：" + (System.currentTimeMillis() - beginTime));
        return object;
    }
}