package com.example.datarestwarehouse.controller;

import com.example.datarestwarehouse.entity.InputProduct;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.InputProductDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.service.InputProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inputProduct")
public class InputProductController {

    ElementNotFound messageProduct = ElementNotFound.PRODUCT;
    ElementNotFound messageInputProduct = ElementNotFound.INPUT_PRODUCT;
    ElementNotFound messageInput = ElementNotFound.INPUT;

    @Autowired
    InputProductService inputProductService;

    @GetMapping
    public HttpEntity<?> getAllInputProducts(@RequestParam int page) {
        Page<InputProduct> allInputProducts = inputProductService.getAllInputProducts(page);
        return ResponseEntity.ok(allInputProducts);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getInputProductById(@PathVariable Integer id) {
        Result result = inputProductService.getInputProductById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/byInputId/{input_id}")
    public HttpEntity<?> getInputProductsByInputId(@PathVariable Integer input_id) {
        List<Result> results = inputProductService.getInputProductsByInputId(input_id);
        return ResponseEntity.status(results.get(0).isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(results);
    }

    @PostMapping
    public HttpEntity<?> addInputProduct(@Valid @RequestBody InputProductDTO inputProductDTO) {
        Result result = inputProductService.addInputProduct(inputProductDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : result.getMessage().equals(messageProduct.getMessage()) ||
                result.getMessage().equals(messageInput.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.FORBIDDEN).body(result);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editInputProductById(@PathVariable Integer id, @Valid @RequestBody InputProductDTO inputProductDTO) {
        Result result = inputProductService.editInputProductById(id, inputProductDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : result.getMessage().equals(messageProduct.getMessage()) ||
                result.getMessage().equals(messageInput.getMessage()) || result.getMessage().equals(messageInputProduct.getMessage()) ?
                HttpStatus.NOT_FOUND : HttpStatus.FORBIDDEN).body(result);
    }


}
