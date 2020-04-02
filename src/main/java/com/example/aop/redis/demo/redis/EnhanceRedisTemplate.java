package com.example.aop.redis.demo.redis;


import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 为了实现分库存储，我们必须重写自带的RedisTemplate，该类继承了springdataredis的RedisTemplate类，
 * 我们加入indexdb为Redis库的编号，重写了里面的RedisConnection方法，加入是否有库值传递进来的逻辑判断。
 *
 * @ClassName: EnhanceRedisTemplate
 * @Auther: eric
 * @Date: 04/02/2020
 * @Description: 重写RedisTemplate, 加入选库功能
 */
public class EnhanceRedisTemplate extends RedisTemplate {

    public static ThreadLocal<Integer> indexdb = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        try {
            Integer dbIndex = indexdb.get();
            //如果设置了dbIndex
            if (dbIndex != null) {
                if (connection instanceof JedisConnection) {
                    if (((JedisConnection) connection).getNativeConnection().getDB().intValue() != dbIndex) {
                        connection.select(dbIndex);
                    }
                } else {
                    connection.select(dbIndex);
                }
            } else {
                connection.select(0);
            }
        } finally {
            indexdb.remove();
        }
        return super.preProcessConnection(connection, existingConnection);
    }
}