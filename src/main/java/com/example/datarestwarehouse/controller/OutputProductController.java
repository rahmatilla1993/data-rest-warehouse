package com.example.datarestwarehouse.controller;

import com.example.datarestwarehouse.entity.OutputProduct;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.OutputProductDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.service.OutputProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/outputProduct")
public class OutputProductController {

    ElementNotFound messageOutputProduct = ElementNotFound.OUTPUT_PRODUCT;
    ElementNotFound messageOutput = ElementNotFound.OUTPUT;
    ElementNotFound messageProduct = ElementNotFound.PRODUCT;

    @Autowired
    OutputProductService outputProductService;

    @GetMapping
    public HttpEntity<?> getAllOutputProducts(@RequestParam int page) {
        Page<OutputProduct> allOutputProducts = outputProductService.getAllOutputProducts(page);
        return ResponseEntity.ok(allOutputProducts);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOutputProductById(@PathVariable Integer id) {
        Result result = outputProductService.getOutputProductById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/byOutputId/{output_id}")
    public HttpEntity<?> getOutputProductsByOutputId(@PathVariable Integer output_id) {
        List<Result> results = outputProductService.getOutputProductsByOutputId(output_id);
        return ResponseEntity.status(results.get(0).isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(results);
    }

    @PostMapping
    public HttpEntity<?> addOutputProduct(@Valid @RequestBody OutputProductDTO outputProductDTO) {
        Result result = outputProductService.addOutputProduct(outputProductDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : result.getMessage().equals(messageOutput.getMessage()) ||
                result.getMessage().equals(messageProduct.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.FORBIDDEN).body(result);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editOutputProductById(@PathVariable Integer id, @Valid @RequestBody OutputProductDTO outputProductDTO) {
        Result result = outputProductService.editOutputProductById(id, outputProductDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : result.getMessage().equals(messageOutputProduct.getMessage()) ||
                result.getMessage().equals(messageOutput.getMessage()) || result.getMessage().equals(messageProduct.getMessage()) ?
                HttpStatus.NOT_FOUND : HttpStatus.FORBIDDEN).body(result);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteOutputProductById(@PathVariable Integer id) {
        Result result = outputProductService.deleteOutputProductById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND).body(result);
    }
}
