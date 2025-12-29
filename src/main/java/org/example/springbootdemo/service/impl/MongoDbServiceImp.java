package org.example.springbootdemo.service.impl;

import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.operators.relational.GeometryDistance;
//import org.example.springbootdemo.mongodbdao.ProductRepository;
import org.example.springbootdemo.mongodbmodel.Product;
import org.example.springbootdemo.service.IMongoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * mongodb 操作的 service
 *
 * mongodb 常用的场景：
 * 总结： MongoDB 最适合那些数据结构相对灵活、读写密集、需要快速开发迭代的应用场景
 * - 内容管理系统 (CMS)：博客文章，每篇文章的内容结构可能不同
 * - 日志和事件数据存储：高写入性能，无需预定义 schema
 * - 电商商品目录：支持复杂的嵌套结构，查询灵活
 * - 实时分析和报告：支持复杂的聚合查询，水平扩展容易
 */
@Slf4j
@Service
public class MongoDbServiceImp implements IMongoDbService {
    /*
    【用自定义 ProductRepository 还是直接的 MongoTemplate】：
    Repository 适合日常开发，MongoTemplate 适合复杂场景，两者结合使用效果最佳

    80% 的场景使用 Repository 接口（简单、快速、易维护）
    20% 复杂场景使用 MongoTemplate（灵活性、性能）

    Repository，代码简洁，约定优于配置，对应简单 CRUD
    MongoTemplate，更灵活，支持复杂查询和聚合操作，运行时构建查询条件，可以精确控制查询行为，适合复杂查询/动态查询/性能优化
    * */

    // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//    // 简单 CRUD 场景时候使用自定义 ProductRepository
//    @Autowired // 注入Repository，类似于注入MyBatis的Mapper
//    @Qualifier("mongoProductRepository") // 指定哪个 bean
//    private ProductRepository productRepository;

    // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//    // 复杂查询时使用 MongoTemplate
//    @Autowired
//    private MongoTemplate mongoTemplate;

    /**
     * 方式一：@Indexed(GeoSpatialIndexType.GEO_2DSPHERE)  // 创建地理空间索引
     *  - @Indexed: 编译时静态定义，适合简单固定的索引
     * 方式二：这里的 @PostConstruct
     *  - @PostConstruct: 运行时动态创建，适合复杂索引策略和条件创建。可以检查索引是否已存在，支持复合地理索引，可以根据环境配置决定是否创建，更灵活的索引管理策略，就看你的项目用不用得到复杂的情况
     *
     * 其实方式二更加灵活，用于复杂场景，方式一适合简单场景
     *
     * 如果使用方式二，需要注意的是：
     * @PostConstruct 不仅仅说可以写在这个 service 中，其实放在 Configuration 类中更规范，使用 CommandLineRunner 应用启动完成后执行也行
     */
    // 创建地理位置索引（需要在应用启动时执行一次）
    @PostConstruct
    public void createGeoIndex() {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        mongoTemplate.createCollection(Product.class);
//        mongoTemplate.getCollection("products").createIndex(Indexes.geo2dsphere("location"));
    }

    /* ========= repository ========= */

    // 保存产品
    // 使用了已有的 MongoRepository 的 save() 能力
    public Product saveProduct(Product product) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        // 在MongoDB中，如果id不存在会创建新文档，存在则更新
//        return productRepository.save(product);
        return null;
    }

    // 获取所有产品
    // 使用了已有的 MongoRepository 的 findAll() 能力
    public List<Product> getAllProducts() {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        return productRepository.findAll();
        return null;
    }

    // 根据ID查找产品
    // 使用了已有的 MongoRepository 的 findById() 能力
    public Optional<Product> getProductById(String id) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        return productRepository.findById(id);
        return null;
    }

    // 根据名称查找产品
    // 使用了自定义的 ProductRepository 的 findByName() 能力
    public List<Product> getProductsByName(String name) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        return productRepository.findByName(name);
        return null;
    }

    // 根据价格范围查找产品
    // 使用了自定义的 ProductRepository 的 findProductsByPriceRange() 能力
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        return productRepository.findProductsByPriceRange(minPrice, maxPrice);
        return null;
    }

    // 根据分类查找产品
    // 使用了自定义的 ProductRepository 的 findByCategory() 能力
    public List<Product> getProductsByCategory(String category) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        return productRepository.findByCategory(category);
        return null;
    }

    // 删除产品
    // 使用了已有的 MongoRepository 的 deleteById() 能力
    public void deleteProduct(String id) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        productRepository.deleteById(id);
    }

    // 根据标签查找产品
    // 使用了自定义的 ProductRepository 的 findByTag() 能力
    public List<Product> getProductsByTag(String tag) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        return productRepository.findByTag(tag);
        return null;
    }

    // 更新产品信息
    // 使用了已有的 MongoRepository 的 save() 能力
    public Product updateProduct(String id, Product updatedProduct) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        updatedProduct.setId(id); // 确保ID不变
//        return productRepository.save(updatedProduct);
        return null;
    }

    // 统计某个分类的产品数量
    // 使用了自定义的 ProductRepository 的 countByCategory() 能力
    public long countProductsByCategory(String category) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        return productRepository.countByCategory(category);
        return 0;
    }

    /* ========= template ========= */

    // 示例1: 动态价格范围查询
    public List<Product> findProductsByPriceRange(Double minPrice, Double maxPrice) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        Query query = new Query();
//        // select: where -> gte/lte
//        query.addCriteria(Criteria.where("price").gte(minPrice).lte(maxPrice));
//        // find
//        return mongoTemplate.find(query, Product.class);

        return null;
    }

    // 示例2: 模糊搜索产品名称
    public List<Product> findProductsByNameLike(String namePattern) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        Query query = new Query();
//        // select: where -> regex
//        query.addCriteria(Criteria.where("name").regex(namePattern, "i"));
//        // find
//        return mongoTemplate.find(query, Product.class);

        return null;
    }

    // 示例3: 批量更新分类产品的价格
    public void updateProductPriceByCategory(String category, Double priceIncrease) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        // select: where -> is
//        Query query = new Query(Criteria.where("category").is(category));
//        // update: where -> inc
//        Update update = new Update().inc("price", priceIncrease);
//        // updateMulti
//        mongoTemplate.updateMulti(query, update, Product.class);
    }

    // 示例4: 为特定产品增加标签
    public void addTagToProduct(String productId, String newTag) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        // select: where -> is
//        Query query = new Query(Criteria.where("_id").is(productId));
//        // update: where -> push
//        Update update = new Update().push("tags", newTag); // push是追加元素到数组
//        // updateFirst
//        mongoTemplate.updateFirst(query, update, Product.class);
    }

    // 示例5: 统计某分类产品数量
    public long countProductsByCategory2(String category) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        // select: where -> is
//        Query query = new Query(Criteria.where("category").is(category));
//        // count
//        return mongoTemplate.count(query, Product.class);

        return 0;
    }

    // 示例6: 原子性价格更新（打折）
    public boolean applyDiscount(String productId, Double discountRate) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        // select: where -> is
//        Query query = new Query(Criteria.where("_id").is(productId));
//        // update: where -> multiply
//        Update update = new Update().multiply("price", (1 - discountRate)); // multiply是乘法运算
//        // updateFirst
//        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
//        return result.getModifiedCount() > 0;

        return false;
    }

    /* ========= geo ========= */

    // 1. 查找附近店铺（5公里内）
    public List<Product> findProductsNearby(double longitude, double latitude, double radiusInKm) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        Query query = new Query();
//        // nearSphere表示球面距离计算，更准确
//        query.addCriteria(Criteria.where("location")
//                .nearSphere(new Point(longitude, latitude))
//                .maxDistance(radiusInKm / 6378.1)); // 转换为弧度
//        return mongoTemplate.find(query, Product.class);

        return null;
    }

    // 2. 查找矩形区域内的店铺
    public List<Product> findProductsInArea(double minLng, double minLat, double maxLng, double maxLat) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        Query query = new Query();
//        // withinBox 定义一个矩形区域
//        query.addCriteria(Criteria.where("location")
//                .within(new Box(new Point(minLng, minLat), new Point(maxLng, maxLat))));
//        return mongoTemplate.find(query, Product.class);

        return null;
    }

    // 3. 查找圆形区域内的店铺
    public List<Product> findProductsInCircle(double centerLng, double centerLat, double radiusInKm) {
        // ⬇️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
//        Query query = new Query();
//        // centerSphere定义一个圆形区域
//        query.addCriteria(Criteria.where("location")
//                .withinSphere(new Circle(new Point(centerLng, centerLat), radiusInKm / 6378.1)));
//        return mongoTemplate.find(query, Product.class);

        return null;
    }

    // 4. 手动计算两点间距离（Haversine公式）
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371; // 地球半径，单位千米

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // 距离，单位千米
    }
}