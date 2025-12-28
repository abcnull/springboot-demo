package org.example.springbootdemo.controller.mongodb;

import org.example.springbootdemo.mongodbmodel.Product;
import org.example.springbootdemo.service.IMongoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * mongodb controller
 *
 * mongodb 中普通的常见的设置 repository，然后 service 中调用 repository，然后 controller 调用 service 的用法
 *
 * mongodb 常用的场景：
 * 总结： MongoDB 最适合那些数据结构相对灵活、读写密集、需要快速开发迭代的应用场景
 * - 内容管理系统 (CMS)：博客文章，每篇文章的内容结构可能不同
 * - 日志和事件数据存储：高写入性能，无需预定义 schema
 * - 电商商品目录：支持复杂的嵌套结构，查询灵活
 * - 实时分析和报告：支持复杂的聚合查询，水平扩展容易
 */
@RestController
@RequestMapping("/mongodb-ordinary")
public class MongoDbSimpleController {
    @Autowired
    private IMongoDbService mongoDbService;

    /**
     * SAVE or UPDATE
     */
    // save，如果 id 不存在会创建新文档，存在则更新
    // 即如果要新增或者更新对应都是原生 save，只不过看 id 存不存在
    @PostMapping("/save")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = mongoDbService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    /**
     * FIND ALL
     */
    // findAll，获取所有产品
    @GetMapping("find-all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = mongoDbService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * FIND BY ID
     */
    // findById，根据ID获取产品
    @GetMapping("search/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = mongoDbService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * FIND BY OTHER
     */
    // 自定义根据 name 查询，根据名称搜索产品
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Product>> searchProductsByName(@PathVariable String name) {
        List<Product> products = mongoDbService.getProductsByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * FIND RANGE
     */
    // 自定义根据价格范围查询，根据价格范围搜索产品
    @GetMapping("/search/price")
    public ResponseEntity<List<Product>> searchProductsByPrice(@RequestParam Double minPrice,
                                                               @RequestParam Double maxPrice) {
        List<Product> products = mongoDbService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * DELETE
     */
    // 通过原生的 delete 方法来删除产品
    @PostMapping("delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        mongoDbService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * COUNT
     */
    // 通过自定义的 countProductsByCategory 方法，统计某个分类的产品数量
    @GetMapping("/count/{category}")
    public ResponseEntity<Long> countProductsByCategory(@PathVariable String category) {
        long count = mongoDbService.countProductsByCategory(category);
        return ResponseEntity.ok(count);
    }

}
