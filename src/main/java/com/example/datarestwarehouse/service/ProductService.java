package com.example.datarestwarehouse.service;

import com.example.datarestwarehouse.entity.Attachment;
import com.example.datarestwarehouse.entity.Category;
import com.example.datarestwarehouse.entity.Measurement;
import com.example.datarestwarehouse.entity.Product;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.ProductDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.repository.AttachmentRepository;
import com.example.datarestwarehouse.repository.CategoryRepository;
import com.example.datarestwarehouse.repository.MeasurementRepository;
import com.example.datarestwarehouse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    ElementNotFound messageProduct = ElementNotFound.PRODUCT;
    ElementNotFound messageCategory = ElementNotFound.CATEGORY;
    ElementNotFound messageFile = ElementNotFound.ATTACHMENT;
    ElementNotFound messageMeasurement = ElementNotFound.MEASUREMENT;

    ElementIsActive categoryActive = ElementIsActive.PRODUCT;
    ElementIsActive measurementActive = ElementIsActive.MEASUREMENT;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    MeasurementRepository measurementRepository;

    public Page<Product> getAllProducts(Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return productRepository.findAll(pageable);
    }

    public Result getProductById(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(product -> new Result(true, product)).orElseGet(() -> new Result(messageProduct.getMessage(), false));
    }

    public List<Result> getProductsByCategoryId(Integer category_id) {
        List<Result> results = new ArrayList<>();
        Optional<Category> optionalCategory = categoryRepository.findById(category_id);
        if (optionalCategory.isPresent()) {
            List<Product> allByCategory_id = productRepository.findAllByCategory_Id(category_id);
            for (Product product : allByCategory_id) {
                Result result = new Result(true, product);
                results.add(result);
            }
            return results;
        }
        Result result = new Result(messageCategory.getMessage(), false);
        results.add(result);
        return results;
    }

    private Result addingProduct(ProductDTO productDTO, boolean create, boolean edit, Integer id) {
        Product product = new Product();
        if (create && productRepository.existsByName(productDTO.getName())) {
            return new Result("Bunday product bor", false);
        }

        Optional<Category> optionalCategory = categoryRepository.findById(productDTO.getCategory_id());
        if (!optionalCategory.isPresent()) {
            return new Result(messageCategory.getMessage(), false);
        }
        Category category = optionalCategory.get();
        if (!category.isActive()) {
            return new Result(categoryActive.getMessageActive(), false);
        }

        Optional<Attachment> optionalAttachment = attachmentRepository.findById(productDTO.getAttachment_id());
        if (!optionalAttachment.isPresent()) {
            return new Result(messageFile.getMessage(), false);
        }
        Attachment attachment = optionalAttachment.get();

        Optional<Measurement> optionalMeasurement = measurementRepository.findById(productDTO.getMeasurement_id());
        if (!optionalMeasurement.isPresent()) {
            return new Result(messageMeasurement.getMessage(), false);
        }
        Measurement measurement = optionalMeasurement.get();
        if (!measurement.isActive()) {
            return new Result(measurementActive.getMessageActive(), false);
        }

        product.setAttachment(attachment);
        product.setCategory(category);
        product.setMeasurement(measurement);
        product.setActive(productDTO.isActive());
        product.setName(productDTO.getName());
        return new Result(true, product);
    }

    private String generateProductCode() {
        int size = productRepository.findAll().size();
        if (size == 0) {
            return "1";
        }
        Product product = productRepository.findAll().get(size - 1);
        return product.getId().toString();
    }

    public Result addProduct(ProductDTO productDTO) {
        Result result = addingProduct(productDTO, true, false, null);
        if (result.isSuccess()) {
            Product product = (Product) result.getObject();
            product.setCode(generateProductCode());
            productRepository.save(product);
            return new Result("Product qo'shildi",true);
        }
    }
}
