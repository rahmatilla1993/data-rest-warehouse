package com.example.datarestwarehouse.service;

import com.example.datarestwarehouse.entity.Input;
import com.example.datarestwarehouse.entity.InputProduct;
import com.example.datarestwarehouse.entity.Product;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.InputProductDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.repository.InputProductRepository;
import com.example.datarestwarehouse.repository.InputRepository;
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
public class InputProductService {

    ElementNotFound messageInputProduct = ElementNotFound.INPUT_PRODUCT;
    ElementNotFound messageInput = ElementNotFound.INPUT;
    ElementNotFound messageProduct = ElementNotFound.PRODUCT;

    ElementIsActive productActive = ElementIsActive.PRODUCT;

    @Autowired
    InputProductRepository inputProductRepository;

    @Autowired
    InputRepository inputRepository;

    @Autowired
    ProductRepository productRepository;

    public Page<InputProduct> getAllInputProducts(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return inputProductRepository.findAll(pageable);
    }

    public Result getInputProductById(Integer id) {
        Optional<InputProduct> optionalInputProduct = inputProductRepository.findById(id);
        return optionalInputProduct.map(inputProduct -> new Result(true, inputProduct)).orElseGet(() ->
                new Result(messageInputProduct.getMessage(), false));
    }

    public List<Result> getInputProductsByInputId(Integer input_id) {

        List<Result> results = new ArrayList<>();
        Optional<Input> optionalInput = inputRepository.findById(input_id);
        if (optionalInput.isPresent()) {
            List<InputProduct> productList = inputProductRepository.findAllByInput_Id(input_id);
            for (InputProduct inputProduct : productList) {
                Result result = new Result(true, inputProduct);
                results.add(result);
            }
            return results;
        }
        Result result = new Result(messageInput.getMessage(), false);
        results.add(result);
        return results;
    }

    private Result addingInputProduct(InputProductDTO inputProductDTO) {
        InputProduct inputProduct = new InputProduct();

        Optional<Product> optionalProduct = productRepository.findById(inputProductDTO.getProduct_id());
        if (!optionalProduct.isPresent()) {
            return new Result(messageProduct.getMessage(), false);
        }
        Product product = optionalProduct.get();
        if (!product.isActive()) {
            return new Result(productActive.getMessageActive(), false);
        }

        Optional<Input> optionalInput = inputRepository.findById(inputProductDTO.getInput_id());
        if (!optionalInput.isPresent()) {
            return new Result(messageInput.getMessage(), false);
        }
        Input input = optionalInput.get();

        inputProduct.setProduct(product);
        inputProduct.setInput(input);
        inputProduct.setAmount(inputProductDTO.getAmount());
        inputProduct.setPrice(inputProductDTO.getPrice());
        inputProduct.setExpireDate(inputProductDTO.getExpireDate());
        return new Result(true, inputProduct);
    }

    public Result addInputProduct(InputProductDTO inputProductDTO) {
        Result result = addingInputProduct(inputProductDTO);
        if (result.isSuccess()) {
            InputProduct inputProduct = (InputProduct) result.getObject();
            inputProductRepository.save(inputProduct);
            return new Result(true, inputProduct);
        }
        return result;
    }

    public Result editInputProductById(Integer id, InputProductDTO inputProductDTO) {
        Optional<InputProduct> optionalInputProduct = inputProductRepository.findById(id);
        if (optionalInputProduct.isPresent()) {
            Result result = addingInputProduct(inputProductDTO);
            if (result.isSuccess()) {
                InputProduct editInputProduct = optionalInputProduct.get();
                InputProduct inputProduct = (InputProduct) result.getObject();
                editInputProduct.setProduct(inputProduct.getProduct());
                editInputProduct.setInput(inputProduct.getInput());
                editInputProduct.setPrice(inputProduct.getPrice());
                editInputProduct.setAmount(inputProduct.getAmount());
                editInputProduct.setExpireDate(inputProduct.getExpireDate());
                inputProductRepository.save(editInputProduct);
                return new Result("Kirim mahsulotlari tahrirlandi", true);
            }
            return result;
        }
        return new Result(messageInputProduct.getMessage(), false);
    }

    public Result deleteInputProductById(Integer id) {
        Optional<InputProduct> optionalInputProduct = inputProductRepository.findById(id);
        if (optionalInputProduct.isPresent()) {
            inputProductRepository.delete(optionalInputProduct.get());
            return new Result("Kirimdagi mahsulot o'chirildi", true);
        }
        return new Result(messageInputProduct.getMessage(), false);
    }
}
