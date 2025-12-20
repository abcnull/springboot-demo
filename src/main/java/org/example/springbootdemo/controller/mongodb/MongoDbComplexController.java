package org.example.springbootdemo.controller.mongodb;

import org.example.springbootdemo.mongodbmodel.Product;
import org.example.springbootdemo.service.IMongoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * mongodb controller
 * <p>
 * mongodb 中使用自带的 MongoTemplate
 * MongoTemplate，更灵活，支持复杂查询和聚合操作，运行时构建查询条件，可以精确控制查询行为，适合复杂查询/动态查询/性能优化
 * 但是 20% 场景可能才适合 MongoTemplate，因为 repository 的方式已经支持大多数场景，且更简单清晰
 *
 * mongodb 常用的场景：
 * 总结： MongoDB 最适合那些数据结构相对灵活、读写密集、需要快速开发迭代的应用场景
 * - 内容管理系统 (CMS)：博客文章，每篇文章的内容结构可能不同
 * - 日志和事件数据存储：高写入性能，无需预定义 schema
 * - 电商商品目录：支持复杂的嵌套结构，查询灵活
 * - 实时分析和报告：支持复杂的聚合查询，水平扩展容易
 */
@RestController
@RequestMapping("/mongodb-complex")
public class MongoDbComplexController {
    @Autowired
    private IMongoDbService mongoDbService;

    /**
     * FIND RANGE
     */
    // 根据价格范围搜索产品
    @GetMapping("/search/price")
    public ResponseEntity<List<Product>> searchProductsByPrice(@RequestParam Double minPrice,
                                                               @RequestParam Double maxPrice) {
        List<Product> products = mongoDbService.findProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * FIND BY OTHER
     */
    // 根据名称模糊搜索产品
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Product>> searchProductsByName(@PathVariable String name) {
        String changedName = name + ""; // 这里对 name 做一些模糊变形

        List<Product> products = mongoDbService.findProductsByNameLike(changedName);
        return ResponseEntity.ok(products);
    }

    /**
     * BATCH UPDATE
     */
    // 批量更新分类产品的价格
    @PostMapping("/update/batch/{name}")
    public String updateBatchPriceByCategory(
            @RequestParam String category, @RequestParam Double priceIncrease) {

        mongoDbService.updateProductPriceByCategory(category, priceIncrease);
        return "success";
    }

    /**
     * UPDATE PUSH
     */
    // 为特定产品增加标签
    @PostMapping("/update/tag")
    public String updateTagById(
            @RequestParam String productId, @RequestParam String newTag) {

        mongoDbService.addTagToProduct(productId, newTag);
        return "success";
    }

    /**
     * UPDATE MULTIPLY
     */
    // 指定产品的原子性价格更新（打折）
    @PostMapping("/update/tag")
    public String updatePriceById(
            @RequestParam String productId, @RequestParam Double discountRate) {

        mongoDbService.applyDiscount(productId, discountRate);
        return "success";
    }

    /**
     * COUNT
     */
    // 统计某个分类的产品数量
    @GetMapping("/count/{category}")
    public ResponseEntity<Long> countProductsByCategory(@PathVariable String category) {
        long count = mongoDbService.countProductsByCategory2(category);
        return ResponseEntity.ok(count);
    }
}
