package org.example.springbootdemo.service;

import org.example.springbootdemo.esmodel.Product;

import java.util.List;

public interface IEsService {
    Product save(Product product);

    List<Product> searchProducts(String keyword);

    List<Product> filterByCategoryAndPrice(String category, double minPrice, double maxPrice);

    Iterable<Product> saveAll(List<Product> products);

    List<Product> complexSearch(String keyword, String category,
                                Double minPrice, Double maxPrice,
                                int page, int size);
}
