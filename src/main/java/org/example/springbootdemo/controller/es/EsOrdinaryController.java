package org.example.springbootdemo.controller.es;

import org.example.springbootdemo.esmodel.Product;
import org.example.springbootdemo.service.IEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * es controller
 * <p>
 * Spring Boot 2.4+ 与 Elasticsearch 7.x 兼容，
 * Spring Boot 3.x 与 Elasticsearch 8.x 兼容
 * <p>
 * 对于重要数据，应当有从数据库到 ES 的同步机制，可使用 Logstash、Canal 或应用层同步
 * 使用过滤器上下文 (filter context) 而非查询上下文 (query context) 进行过滤
 * <p>
 * es 经典常用业务场景：
 * - 全文搜索（电商/内容平台）
 * - 日志分析与监控（ELK栈）即 es + Logstash + Kibana
 * - 直接通过 springboot 中 Logback 对应 xml 日志配置，配置上发送到 ES
 * - es 存储
 * - kibana 可视化查询
 * - 实时数据分析和可视化
 * - 个性化推荐系统
 * - 自动补全和搜索建议
 * <p>
 * 可以使用 ElasticsearchRestTemplate 这个模版来做一些复杂的查询
 */
@RestController
@RequestMapping("/es-ordinary")
public class EsOrdinaryController {
    @Autowired
    private IEsService esService;

    // 依据多项条件做复杂条件商品查询
    @GetMapping("/search")
    public List<Product> ordinarySearch(
            @RequestParam String keyword, @RequestParam String category,
            @RequestParam Double minPrice, @RequestParam Double maxPrice,
            @RequestParam int page, @RequestParam int size) {
        return esService.complexSearch(keyword, category, minPrice, maxPrice, page, size);
    }
}
