package org.example.springbootdemo.service;

import org.example.springbootdemo.mongodbmodel.Product;

import java.util.List;
import java.util.Optional;

public interface IMongoDbService {

    /* ========= repository ========= */

    // 保存产品
    Product saveProduct(Product product);

    // 获取所有产品
    List<Product> getAllProducts();

    // 根据ID查找产品
    Optional<Product> getProductById(String id);

    // 根据名称查找产品
    List<Product> getProductsByName(String name);

    // 根据价格范围查找产品
    List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice);

    // 根据分类查找产品
    List<Product> getProductsByCategory(String category);

    // 删除产品
    void deleteProduct(String id);

    // 根据标签查找产品
    List<Product> getProductsByTag(String tag);

    // 更新产品信息
    Product updateProduct(String id, Product updatedProduct);

    // 统计某个分类的产品数量
    long countProductsByCategory(String category);

    /* ========= template ========= */

    List<Product> findProductsByPriceRange(Double minPrice, Double maxPrice);

    List<Product> findProductsByNameLike(String namePattern);

    void updateProductPriceByCategory(String category, Double priceIncrease);

    void addTagToProduct(String productId, String newTag);

    long countProductsByCategory2(String category);

    boolean applyDiscount(String productId, Double discountRate);

    List<Product> findProductsNearby(double longitude, double latitude, double radiusInKm);

    List<Product> findProductsInArea(double minLng, double minLat, double maxLng, double maxLat);

    List<Product> findProductsInCircle(double centerLng, double centerLat, double radiusInKm);

    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
}
