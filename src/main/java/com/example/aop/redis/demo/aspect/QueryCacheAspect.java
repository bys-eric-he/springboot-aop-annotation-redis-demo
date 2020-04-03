package com.example.aop.redis.demo.aspect;

import com.example.aop.redis.demo.annotation.AOPCacheKey;
import com.example.aop.redis.demo.annotation.ParameterCacheKey;
import com.example.aop.redis.demo.common.AOPMethodUtil;
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
public class QueryCacheAspect {

    private RedisUtil redisUtil;

    @Autowired
    public QueryCacheAspect(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 定义拦截规则：拦截所有@QueryCache注解的方法。
     */
    @Pointcut("((!execution(* com.example.aop.redis.demo.service.impl..*.update*(..))) " +
            "&& (!execution(* com.example.aop.redis.demo.service.impl..*.delete*(..)))) " +
            "&& @annotation(com.example.aop.redis.demo.annotation.AOPCacheKey)")
    public void queryCachePointcut() {
    }

    /**
     * 拦截器具体实现
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("queryCachePointcut()")
    public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        log.info("------- 进入 AOP 缓存切面处理 ------- ");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取被拦截的方法
        Method method = signature.getMethod();
        //获取方法注解
        AOPCacheKey aopCacheKey = method.getAnnotation(AOPCacheKey.class);
        StringBuilder key =
                new StringBuilder(aopCacheKey.nameSpace().name() + "_" + aopCacheKey.key());

        // 循环所有的参数
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            MethodParameter methodParam = new SynthesizingMethodParameter(method, i);
            //获取参数注解
            Annotation[] parameterAnnotations = methodParam.getParameterAnnotations();
            // 循环参数上所有的注解
            for (Annotation paramAnn : parameterAnnotations) {
                if (paramAnn instanceof ParameterCacheKey) {
                    ParameterCacheKey parameterCacheKey = (ParameterCacheKey) paramAnn;
                    //取到ParameterCacheKey的标识参数的值
                    Object fieldValue = AOPMethodUtil.getParamValue(joinPoint, parameterCacheKey.fieldName());
                    key.append("_").append(parameterCacheKey.fieldName()).append("_").append(fieldValue);
                    break;
                }
            }
        }

        //获取不到key值，抛异常
        if (StringUtils.isBlank(key.toString())) throw new Exception("****缓存key值不存在****");

        log.info("获取到缓存key值 -> " + key);
        boolean hasKey = redisUtil.hasKey(key.toString());
        if (hasKey) {

            // 缓存中获取到数据，直接返回。
            Object object = redisUtil.get(key.toString(), RedisConstants.DataBase1.getValue());
            log.info("从缓存中获取到数据 -> " + object.toString());
            log.info("-------- 结束AOP 缓存切面处理 ------- 耗时：" + (System.currentTimeMillis() - beginTime));
            return object;
        }

        //缓存中没有数据，调用原始方法查询数据库
        Object object = joinPoint.proceed();
        //设置超时时间
        redisUtil.set(key.toString(), object, RedisConstants.DataBase1.getValue(), aopCacheKey.expireTime(),
                aopCacheKey.timeUnit());

        log.info("DB取到数据并存入缓存 -> " + object.toString());
        log.info("-------- 结束AOP 缓存切面处理 ------- 耗时：" + (System.currentTimeMillis() - beginTime));
        return object;
    }
}