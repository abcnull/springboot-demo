package org.example.springbootdemo.mongodbmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * product mongodb model
 */
@Document(collection = "products") // 指定MongoDB集合名称
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /*
    【@Id】
    - MongoDB的_id字段，会自动生成ObjectId
    - 此注解来源 springboot data 而非 mongodb

    【关于 String 类型】
    - 定义 String 类型是标准做法！int/Integer 不能用作 MongoDB 主键
    - MongoDB的ObjectId 是 24 位十六进制字符串，不是数字

    【id】
    - 定义时候写的是 id，但是在实际 mongodb 中查询时候是 _id
    * */
    @Id
    private String id;

    /*
    【@Field】
    - 指定在MongoDB中存储的字段名。此注解来源 mongodb

    【@Indexed】
    - 注意不要每个字段都加上索引，要高频查询的字段，排序字段加上！
    - @Indexed 表示普通索引，加速查询
    - @Indexed(expireAfter = "1d") TTL索引，1天后过期
    - @Indexed(unique = true) 单字段唯一索引
    - @Indexed(background = true) 后台创建索引，不影响数据库性能
    * */
    @Field("name")
    @Indexed
    private String name;

    @Field("description")
    private String description;

    @Field("price")
    private Double price;

    @Field("category")
    private String category;

    @Field("tags")
    private List<String> tags; // MongoDB原生支持数组类型

    // MongoDB 地理位置字段，使用 Point 类型
    @Field("location")
    private Point location;  // Point 是 org.springframework.data.geo.Point

    @Field("created_at")
    @CreatedDate  // 创建时自动填充，但是必须结合配置类 MongoConfig，否则仅仅是标准不起作用！
    private LocalDateTime createdAt;

    @Field("updated_at")
    @LastModifiedDate  // 更新时自动填充，但是必须结合配置类 MongoConfig，否则仅仅是标准不起作用！
    private LocalDateTime updatedAt;
}