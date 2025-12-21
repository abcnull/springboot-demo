package org.example.springbootdemo.controller.es;

import org.example.springbootdemo.esmodel.Product;
import org.example.springbootdemo.service.IEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
 * 可以使用自定义的 repository 这个 dao 层来进行查询
 */
@RestController
@RequestMapping("/es-simple")
public class EsSimpleController {
    @Autowired
    private IEsService esService;

    // 存储商品进 es
    // 其实写入时，可以直接操作 es 写入，开发中也有用传入 Kafka 后，消费者拿到消息再写入 es 的，如果通过消息的方式就可以解放生产者服务和 es 存储的耦合避免 es 存储失败而导致写入服务后续阻碍
    @PostMapping("/create")
    public Product createProduct(@RequestBody Product product) {
        return esService.save(product);
    }

    // 依据关键词搜索商品
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return esService.searchProducts(keyword);
    }

    // 按照分类和按照价格范围查询商品
    @GetMapping("/filter")
    public List<Product> filterProducts(@RequestParam String category,
                                        @RequestParam double minPrice, @RequestParam double maxPrice) {
        return esService.filterByCategoryAndPrice(category, minPrice, maxPrice);
    }
}
