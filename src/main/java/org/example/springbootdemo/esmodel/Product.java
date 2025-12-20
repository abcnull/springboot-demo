package org.example.springbootdemo.esmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * es model
 * <p>
 * 电商搜索商品
 *
 * 初期数据量小可以设置1-3个分片，数据量大时根据集群节点数和预期数据量规划
 * 不需要搜索的字段设置index = false
 * 不需要高亮的字段设置store = false
 * 频繁聚合的字段可设置fielddata = true（但注意内存消耗）
 */
@Document(indexName = "products",
        createIndex = true, // 启动时自动创建索引
        replicas = 1, // 副本数量
        shards = 3 // 分片数量
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;

    /**
     * 中文分词，需要在 Elasticsearch 中安装 IK 分词器
     *
     * type = FieldType.Text 表示该字段是全文搜索文本字段，会进行分词处理，支持全文检索
     *
     * analyzer = "ik_max_word"，索引时使用的分析器，表示中文分词策略 - 最细粒度切分。索引时用 ik_max_word：尽可能多地建立索引词项，提高召回率
     * 举例子：
     * 例如："中华人民共和国国歌" 会被切分为：
     * 中华人民共和国
     * 中华人民
     * 中华
     * 华人
     * 人民共和国
     * 人民
     * 共和国
     * 共和
     * 国
     * 国歌
     *
     * searchAnalyzer = "ik_smart"，搜索时使用的分析器，中文分词策略 - 智能切分（粗粒度）。搜索时用 ik_smart：减少搜索词的分词数量，提高精确度和性能
     * 例如："中华人民共和国国歌" 会被切分为：
     * 中华人民共和国
     * 国歌
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String name;

    // 中文分词，需要在 Elasticsearch 中安装 IK 分词器
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String description;

    @Field(type = FieldType.Double)
    private double price;

    /**
     * FieldType.Keyword 表示该字段不分词，用于精确匹配、聚合、排序
     */
    @Field(type = FieldType.Keyword)
    private String category;

    // 日期字段：用于时间范围查询和排序
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // 不需要搜索但需要返回的字段
    @Field(type = FieldType.Text, index = false) // 不建立索引
    private String others;


}
