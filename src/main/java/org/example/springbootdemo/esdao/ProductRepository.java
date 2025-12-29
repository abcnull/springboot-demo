//package org.example.springbootdemo.esdao;
//
//import org.example.springbootdemo.esmodel.Product;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
///**
// * es dao
// * <p>
// * 自定义的这些方法即使没有实现，Spring Data 也会根据方法名自动实现
// * 所以这些自定义方法需要满足 spring data 的方法命名规则，否则 springboot 项目在启动时后就会报错
// * 这一块参照 mongodb 也是一样的逻辑
// */
//@Repository("esProductRepository")
//public interface ProductRepository extends ElasticsearchRepository<Product, String> {
//    // Spring Data会自动实现这些方法
//    List<Product> findByNameContaining(String name);
//
//    List<Product> findByCategory(String category);
//
//    List<Product> findByPriceBetween(double minPrice, double maxPrice);
//
//    // 复合查询
//    List<Product> findByCategoryAndPriceBetween(String category, double minPrice, double maxPrice);
//}
//
//// ⬆️ 为了保证项目成功启动，这里需要注释掉，当你真的存在 es 服务端时候，yml 中设置好连接，这里解开注释
