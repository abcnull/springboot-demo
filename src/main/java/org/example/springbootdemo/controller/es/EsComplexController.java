package org.example.springbootdemo.controller.es;

import org.example.springbootdemo.esmodel.Product;
import org.example.springbootdemo.service.IEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * es controller
 */
@RestController
@RequestMapping("/es-complex")
public class EsComplexController {
    @Autowired
    private IEsService esService;

    // 存储商品进 es
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
