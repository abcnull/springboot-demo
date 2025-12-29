//package org.example.springbootdemo.mongodbdao;
//
//import org.example.springbootdemo.mongodbmodel.Product;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
///**
// * mongodb dao，即 repository 数据库访问层
// *
// * 为什么 Repository 接口不需要实现类的原因？
// * 因为 Spring Data 使用了代理模式和 AOP，Mongo 又是在 Spring Data 中的
// * 当你 service 中直接注入 ProductRepository 后，
// * Spring 启动时扫描所有继承 MongoRepository 的接口，为每个 Repository 接口动态生成实现类，解析方法名（如findByCategory）并转换为 MongoDB 查询
// *
// * 所以 Spring Data 的查询方法命名有一套规则，如果不遵循 Spring Data 命名规则就会在 springboot 项目启动时候就会发生报错
// * // findBy + 字段名
// * List<Product> findByName(String name);           // name字段相等
// * List<Product> findByCategory(String category);   // category字段相等
// * // And/Or组合
// * List<Product> findByNameAndCategory(String name, String category);
// * List<Product> findByNameOrCategory(String name, String category);
// * // 比较操作
// * List<Product> findByPriceGreaterThan(Double price);    // >
// * List<Product> findByPriceLessThanEqual(Double price);  // <=
// * List<Product> findByPriceBetween(Double min, Double max); // between
// * // 模糊匹配
// * List<Product> findByNameLike(String name);        // LIKE
// * List<Product> findByNameStartingWith(String prefix); // STARTS WITH
// * List<Product> findByNameEndingWith(String suffix);   // ENDS WITH
// */
//@Repository("mongoProductRepository") // 相当于MyBatis中的@Mapper
//public interface ProductRepository extends MongoRepository<Product, String> {
//
//    // 方法名查询 - Spring Data会自动解析方法名生成查询
//    List<Product> findByName(String name);
//
//    List<Product> findByCategory(String category);
//
//    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
//
//    List<Product> findByCategoryAndPriceLessThan(String category, Double maxPrice);
//
//    // 模糊查询
//    List<Product> findByNameContainingIgnoreCase(String name);
//
//    // 自定义查询 - 使用MongoDB查询语法
//    @Query("{'price': {$gte: ?0, $lte: ?1}}")
//    List<Product> findProductsByPriceRange(Double minPrice, Double maxPrice);
//
//    @Query("{'tags': {$in: [?0]}}")
//    List<Product> findByTag(String tag);
//
//    // 统计查询
//    long countByCategory(String category);
//}
//
//// ⬆️ 因为没有 mongodb 服务端，如果 mongo 服务端 ok 可以开放这里
