package com.example.datarestwarehouse.controller;

import com.example.datarestwarehouse.entity.Product;
import com.example.datarestwarehouse.models.ProductDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public HttpEntity<?> getAllProducts(@RequestParam int page) {
        Page<Product> allProducts = productService.getAllProducts(page);
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getProductById(@PathVariable Integer id) {
        Result result = productService.getProductById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/byCategoryId/{category_id}")
    public HttpEntity<?> getProductByCategoryId(@PathVariable Integer category_id) {
        List<Result> results = productService.getProductsByCategoryId(category_id);
        return ResponseEntity.status(results.get(0).isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(results);
    }

    @PostMapping
    public HttpEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO){

    }
}
