package org.example.springbootdemo.controller.mongodb;

import org.example.springbootdemo.mongodbmodel.Product;
import org.example.springbootdemo.service.IMongoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * mongodb controller
 * <p>
 * 用于处理 mongodb geo 相关的请求
 */
@RestController
@RequestMapping("/mongodb-geo")
public class MongoDbGeoController {
    @Autowired
    private IMongoDbService mongoDbService;

    /**
     * 指定经纬度查询附近的产品
     */
    @GetMapping("/nearby")
    public String nearby() {
        double longitude = 114.305556;
        double latitude = 22.543056;
        double radiusInKm = 10.0;
        List<Product> products = mongoDbService.findProductsNearby(longitude, latitude, radiusInKm);
        System.out.println("nearby products: " + products);
        return "success";
    }

    /**
     * 指定矩形区域查询产品
     */
    @GetMapping("/nearby-rect")
    public String nearbyRect() {
        double minLng = 114.305556;
        double minLat = 22.543056;
        double maxLng = 114.305556;
        double maxLat = 22.543056;
        List<Product> products = mongoDbService.findProductsInArea(minLng, minLat, maxLng, maxLat);
        System.out.println("nearby rect products: " + products);
        return "success";
    }

    /**
     * 指定圆区域查询产品
     */
    @GetMapping("/nearby-circle")
    public String nearbyCircle() {
        double longitude = 114.305556;
        double latitude = 22.543056;
        double radiusInKm = 10.0;
        List<Product> products = mongoDbService.findProductsInCircle(longitude, latitude, radiusInKm);
        System.out.println("nearby circle products: " + products);
        return "success";
    }

    /**
     * 计算指定点到产品的距离
     */
    @GetMapping("/distance")
    public String calDistance() {
        double lat1 = 22.543056;
        double lon1 = 114.305556;
        double lat2 = 22.543056;
        double lon2 = 114.305556;
        double distance = mongoDbService.calculateDistance(lat1, lon1, lat2, lon2);
        System.out.println("distance: " + distance);
        return "success";
    }
}
