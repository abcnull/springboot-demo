package org.example.springbootdemo.service;

import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.domain.geo.GeoLocation;

import java.util.List;
import java.util.Set;

/**
 * redis 操作
 */
public interface IRedisService {

    // set string
    void setStr(String key, String val);

    // get string
    String getStr(String key);

    // set Obj
    void setObj(String key, String val);

    // get Obj
    Object getObj(String key);

    // set list
    void setList(String key, String val);

    // get list
    List<String> getList(String key);

    // set set
    void setSet(String key, String val);

    // get set
    Set<String> getSet(String key);

    // set sorted set
    void setSortedSet(String key, String val, double score);

    // get sorted set
    Set<String> getSortedSet(String key);

    // set hash
    void setHash(String key, String mapKey, String mapVal);

    // get hash
    String getHash(String key, String mapKey);

    // set geo
    void setGeo(String key, Point point, String name);

    // get geo
    GeoResults<GeoLocation<String>> getGeo(String key, Point point, double nearby);

    // 原子操作需要用 multi() 和 exec() 实现事务
    void atomic();
}
