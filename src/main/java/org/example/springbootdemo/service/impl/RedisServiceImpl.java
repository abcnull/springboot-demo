package org.example.springbootdemo.service.impl;

import org.example.springbootdemo.constant.RedisConstant;
import org.example.springbootdemo.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis 操作
 */
@Service
public class RedisServiceImpl implements IRedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // set string
    @Override
    public void setStr(String key, String val) {
        stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_PREFIX + key, val, 10, TimeUnit.SECONDS);
    }
    // get string
    @Override
    public String getStr(String key) {
        return stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_PREFIX + key);
    }

    // set Obj
    @Override
    public void setObj(String key, String val) {
        redisTemplate.opsForValue().set(RedisConstant.REDIS_PREFIX + key, val, 10, TimeUnit.SECONDS);
    }
    // get Obj
    @Override
    public Object getObj(String key) {
        return redisTemplate.opsForValue().get(RedisConstant.REDIS_PREFIX + key);
    }

    // set list
    @Override
    public void setList(String key, String val) {
//        // 左推入列表（头部插入）
//        redisTemplate.opsForList().leftPush("logs", "Error: 404");

        // 尾推入
        redisTemplate.opsForList().rightPush(RedisConstant.REDIS_PREFIX + key, val);

//        // 批量右推入列表
//        redisTemplate.opsForList().rightPushAll("queue", "task1", "task2", "task3");
    }
    // get list
    @Override
    public List<String> getList(String key) {
        // 获取列表全部元素（从索引0到-1
        return redisTemplate.opsForList().range(RedisConstant.REDIS_PREFIX + key, 0, -1);

//        // 获取并删除左端元素（类似 LPOP）
//        String task = (String) redisTemplate.opsForList().leftPop("queue");
    }

    // set set
    @Override
    public void setSet(String key, String val) {
        redisTemplate.opsForSet().add(RedisConstant.REDIS_PREFIX + key, val);

//        // 批量添加元素
//        redisTemplate.opsForSet().add("tags", "spring", "redis", "cloud");
    }
    // get set
    @Override
    public Set<String> getSet(String key) {
        // 获取集合所有元素
        return redisTemplate.opsForSet().members(RedisConstant.REDIS_PREFIX + key);

//        // 判断元素是否存在
//        boolean isJavaPresent = redisTemplate.opsForSet().isMember("tags", "java");
    }

    // set sorted set
    @Override
    public void setSortedSet(String key, String val, double score) {
        // 添加元素并指定分数（ZADD
        redisTemplate.opsForZSet().add(RedisConstant.REDIS_PREFIX + key, val, score);
    }
    // get sorted set
    @Override
    public Set<String> getSortedSet(String key) {
        // 获取有序集合的全部元素（按分数升序）
        return redisTemplate.opsForZSet().range(RedisConstant.REDIS_PREFIX + key, 0, -1);

//        // 获取元素的分数
//        Double score = redisTemplate.opsForZSet().score("rankings", "player1");

//        // 获取指定分数范围内的元素（例如分数 > 100）
//        Set<String> highRankers = redisTemplate.opsForZSet().rangeByScore("rankings", 100.0, Double.POSITIVE_INFINITY);
    }

    // set hash
    @Override
    public void setHash(String key, String mapKey, String mapVal) {
        redisTemplate.opsForHash().put(RedisConstant.REDIS_PREFIX + key, mapKey, mapVal);
        // hash 无法单独设置 field 的过期时间，需结合 Sorted Set 实现
        

//        // 批量存储字段
//        Map<String, String> user = new HashMap<>();
//        user.put("name", "Charlie");
//        user.put("email", "charlie@example.com");
//        redisTemplate.opsForHash().putAll("user:1002", user);
    }
    // get hash
    @Override
    public String getHash(String key, String mapKey) {
        return (String) redisTemplate.opsForHash().get(RedisConstant.REDIS_PREFIX + key, mapKey);

//        // 获取所有字段和值
//        Map<Object, Object> userMap = redisTemplate.opsForHash().entries("user:1002");
//
//        // 判断字段是否存在
//        boolean hasAge = redisTemplate.opsForHash().hasKey("user:1001", "age");
    }

    // set geo
    @Override
    public void setGeo(String key, Point point, String name) {
        redisTemplate.opsForGeo().add(RedisConstant.REDIS_PREFIX + key, point, name);
    }
    // get geo
    @Override
    public GeoResults<GeoLocation<String>> getGeo(String key, Point point, double nearby) {
        // 查询某个位置附近的所有点（半径 nearby 公里）
        Circle circle = new Circle(point, new Distance(nearby, Metrics.KILOMETERS));
        return redisTemplate.opsForGeo().radius(RedisConstant.REDIS_PREFIX + key, circle);
    }

    // 原子操作需要用 multi() 和 exec() 实现事务
    @Override
    public void atomic() {
        // 本质是底层使用的是 Redis 的 MULTI/EXEC 命令
        redisTemplate.multi();
        redisTemplate.opsForValue().set("key1", "value1", 10, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("key2", "value2", 10, TimeUnit.SECONDS);
        List<Object> results = redisTemplate.exec(); // 提交事务并获取结果
    }
}