package com.example.aop.redis.demo.aspect;

import com.example.aop.redis.demo.annotation.ClearCacheKey;
import com.example.aop.redis.demo.annotation.QueryCache;
import com.example.aop.redis.demo.common.CacheNameSpace;
import com.example.aop.redis.demo.redis.RedisUtil;
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
 * 清除缓存切面
 */
@Aspect
@Service
@Slf4j
@Order(2)
public class ClearCacheAspect {
    private RedisUtil redisUtil;

    @Autowired
    public ClearCacheAspect(RedisUtil redisUtil) {
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
                if (paramAnn instanceof ClearCacheKey) {
                    ClearCacheKey clearCacheKey = (ClearCacheKey) paramAnn;
                    //取到QueryCacheKey的标识参数的值
                    key = cacheType + "_" + clearCacheKey.keyName() + "_" + value;
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
            redisUtil.del(key);
            log.info("从缓存中删除缓存键 >>>> " + key);
            log.info("AOP 缓存切面处理 >>>> end 耗时：" + (System.currentTimeMillis() - beginTime));
        }

        //缓存中没有数据直接忽略, 继续执行方法本身的逻辑
        return pjp.proceed();
    }
}
