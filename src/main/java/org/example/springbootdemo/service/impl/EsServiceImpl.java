package org.example.springbootdemo.service.impl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.example.springbootdemo.esdao.ProductRepository;
import org.example.springbootdemo.esmodel.Product;
import org.example.springbootdemo.service.IEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * es service
 *
 * Spring Boot 2.4+ 与 Elasticsearch 7.x 兼容，
 * Spring Boot 3.x 与 Elasticsearch 8.x 兼容
 *
 * 对于重要数据，应当有从数据库到 ES 的同步机制，可使用 Logstash、Canal 或应用层同步
 * 使用过滤器上下文 (filter context) 而非查询上下文 (query context) 进行过滤
 *
 * es 经典常用业务场景：
 * - 全文搜索（电商/内容平台）
 * - 日志分析与监控（ELK栈）即 es + Logstash + Kibana
 *  - 直接通过 springboot 中 Logback 对应 xml 日志配置，配置上发送到 ES
 *  - es 存储
 *  - kibana 可视化查询
 * - 实时数据分析和可视化
 * - 个性化推荐系统
 * - 自动补全和搜索建议
 */
@Service
public class EsServiceImpl implements IEsService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /* ========== repository ========== */

    // 存储商品，已有 save 方法
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // 关键词搜索，自定义 findByNameContaining 方法
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    // 依据分类和价格区间查询，自定义 findByCategoryAndPriceBetween 方法
    public List<Product> filterByCategoryAndPrice(String category, double minPrice, double maxPrice) {
        return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
    }

    // 批量保存商品，已有 saveAll 方法
    public Iterable<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    /* ========== template ========== */

    /**
     * 复杂查询商品
     *
     * @param keyword  关键词查询
     * @param category 分类
     * @param minPrice 最低价
     * @param maxPrice 最高价
     * @param page     pageNum
     * @param size     pageSize
     * @return 商品
     */
    public List<Product> complexSearch(String keyword, String category,
                                       Double minPrice, Double maxPrice,
                                       int page, int size) {
        // 构建布尔查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 添加名称匹配
        if (keyword != null && !keyword.isEmpty()) {
            // 名称匹配
            boolQuery.must(QueryBuilders.matchQuery("name", keyword));
        }

        // 添加类别过滤
        if (category != null && !category.isEmpty()) {
            // 过滤类别
            boolQuery.filter(QueryBuilders.termQuery("category.keyword", category));
        }

        // 添加价格范围
        if (minPrice != null || maxPrice != null) {
            BoolQueryBuilder priceQuery = QueryBuilders.boolQuery();
            if (minPrice != null) {
                priceQuery.filter(QueryBuilders.rangeQuery("price").gte(minPrice));
            }
            if (maxPrice != null) {
                priceQuery.filter(QueryBuilders.rangeQuery("price").lte(maxPrice));
            }
            // 过滤价格
            boolQuery.filter(priceQuery);
        }

        // 构建查询，添加分页
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(page, size))
                .build();

        // 执行查询
        SearchHits<Product> searchHits = elasticsearchRestTemplate.search(query, Product.class);

        // 转换结果
        return searchHits.stream().map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
