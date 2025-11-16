package org.example.springbootdemo.controller.redis;

import com.alibaba.fastjson.JSON;
import org.example.springbootdemo.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis 连接池
 */
@RestController
@RequestMapping("/redis-lettuce")
public class LettuceController {
    @Autowired
    private IRedisService redisService;

    // redis 连接池 lettuce 配置
    @GetMapping("/config")
    public String config() {
        String str = "redis 基本配置：在 application.yml 中配置完基本就可以拿 @Autowire 的 RedisTemplate 和 StringRedisTemplate 直接使用了。" +
                "StringRedisTemplate 主要操作 StringRedisTemplate，RedisTemplate 主要操作其它。" +
                "如果有一些复杂配置，比如有第二个 redis 数据源或者 redis 想要配置序列化方式等，可以起一个 @Configuration 配置 RedisConnectionFactory 和 RedisTemplate 的 @Bean。" +
                "springboot2 默认支持 lettuce redis 连接池，还有其他好用的比如 jedis 和 redission，它们都有着自己的特性，这里以 springboot 推荐的 lettuce 为配置例子演示。" +
                "lettuce 连接池的特点是：一个 lettuce 连接可以被多个线程复用，即通过共享一个物理连接（shareNativeConnection=true）来实现多线程复用，不需要频繁创建连接，其实是通过 Netty 实现了异步非阻塞，单连接即可支持高并发。虽然多个线程可以共享一个 lettuce 的一个连接，但是 redis 它本身是串行执行命令的。" +
                "默认情况下（shareNativeConnection = true），Lettuce 的连接池（如 GenericObjectPool）实际上 不会生效，因为所有线程都共用一个连接。" +
                "如果你希望启用连接池（例如为每个线程分配独立连接），需要显式设置 shareNativeConnection = false，此时连接池参数（如 max-active、max-idle）才会起作用。" +
                "如果默认按照 shareNativeConnection = true，Lettuce 会始终使用一个物理连接，所有线程共享这个连接，不会生成第二个新的连接，连接池参数不生效，官方推荐默认值，因为已经适合了大多数场景，单个连接即可满足高并发需求，避免资源浪费。" +
                "Lettuce 在不同线程间切换复用依赖于 Netty 的事件循环调度，而不是操作系统线程的切换。" +
                "如果高并发写入场景，共享连接可能导致事件循环队列过长，影响性能。此时可配置连接池，允许每个线程使用独立连接。" +
                "shareNativeConnection 在哪配置呢？在 RedisConfig 中 RedisConnectionFactory 中配置即可，无法在 application.yml 中配置";
        System.out.println(str);
        return "success";
    }

    // 创建时候设置过期时间
    @GetMapping("/expire")
    public String expire() {
        // 设置 string 且带有过期时间
        redisService.setStr("strKey", "strVal");
        return "success";
    }

    // set/get string
    @GetMapping("/set-get-str")
    public String setGetStr() {
        // 设置 string 且带有过期时间
        redisService.setStr("strKey", "strVal");
        System.out.println(redisService.getStr("strKey"));
        return "success";
    }

    // set/get string
    @GetMapping("/set-get-list")
    public String setGetList() {
        // set/get list
        redisService.setList("listKey", "listVal");
        System.out.println(JSON.toJSONString(
                redisService.getList("listKey")
        ));
        return "success";
    }

    // set/get set
    @GetMapping("/set-get-set")
    public String setGetSet() {
        // set/get set
        redisService.setSet("setKey", "setVal");
        System.out.println(JSON.toJSONString(
                redisService.getSet("setKey")
        ));
        return "success";
    }

    // set/get zset
    @GetMapping("/set-get-zset")
    public String setGetZset() {
        // set/get zset
        redisService.setSortedSet("zsetKey", "zsetVal", 60.0);
        System.out.println(JSON.toJSONString(
                redisService.getSortedSet("zsetKey")
        ));
        return "success";
    }

    // set/get hash
    @GetMapping("/set-get-hash")
    public String setGetHash() {
        // set/get hash
        redisService.setHash("hashKey", "hashVal_Key", "hashVal_Val");
        System.out.println(
                redisService.getHash("hashKey", "hashVal_Key")
        );
        return "success";
    }

    // set/get obj
    @GetMapping("/set-get-obj")
    public String setGetObj() {
        // set/get obj
        redisService.setObj("objKey", "objVal");
        System.out.println(JSON.toJSONString(
                redisService.getObj("objKey")
        ));
        return "success";
    }

    // set/get geo
    @GetMapping("/set-get-geo")
    public String setGetGeo() {
        // set/get geo
        Point point = new Point(116.397428, 39.90923);
        redisService.setGeo("geoKey", point, "beijing");
        System.out.println(JSON.toJSONString(
                redisService.getGeo("geoKey", point, 1)
        ));
        return "success";
    }

    // redis 自带的事务操作
    @GetMapping("/atomic")
    public String atomic() {
        // 本质是底层使用的是 Redis 的 MULTI/EXEC 命令
        redisService.atomic();
        return "success";
    }
}
