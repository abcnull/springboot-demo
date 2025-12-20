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
 */
@RestController
@RequestMapping("/es-ordinary")
public class EsOrdinaryController {
    @Autowired
    private IEsService esService;

    // 依据多项条件做复杂条件商品查询
    @GetMapping("/search")
    public List<Product> complexSearch(
            @RequestParam String keyword, @RequestParam String category,
            @RequestParam Double minPrice, @RequestParam Double maxPrice,
            @RequestParam int page, @RequestParam int size) {
        return esService.complexSearch(keyword, category, minPrice, maxPrice, page, size);
    }
}
