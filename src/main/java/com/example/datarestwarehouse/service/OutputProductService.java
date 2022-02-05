package com.example.datarestwarehouse.service;

import com.example.datarestwarehouse.entity.Output;
import com.example.datarestwarehouse.entity.OutputProduct;
import com.example.datarestwarehouse.entity.Product;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.OutputProductDTO;
import com.example.datarestwarehouse.models.ProductDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.repository.OutputProductRepository;
import com.example.datarestwarehouse.repository.OutputRepository;
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
public class OutputProductService {

    ElementNotFound messageOutputProduct = ElementNotFound.OUTPUT_PRODUCT;
    ElementNotFound messageOutput = ElementNotFound.OUTPUT;
    ElementNotFound messageProduct = ElementNotFound.PRODUCT;
    ElementIsActive productActive = ElementIsActive.PRODUCT;

    @Autowired
    OutputProductRepository outputProductRepository;

    @Autowired
    OutputRepository outputRepository;

    @Autowired
    ProductRepository productRepository;

    public Page<OutputProduct> getAllOutputProducts(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return outputProductRepository.findAll(pageable);
    }

    public Result getOutputProductById(Integer id) {
        Optional<OutputProduct> optionalOutputProduct = outputProductRepository.findById(id);
        return optionalOutputProduct.map(outputProduct -> new Result(true, outputProduct)).orElseGet(() ->
                new Result(messageOutputProduct.getMessage(), false));
    }

    public List<Result> getOutputProductsByOutputId(Integer output_id) {
        List<Result> results = new ArrayList<>();
        Optional<Output> optionalOutput = outputRepository.findById(output_id);
        if (optionalOutput.isPresent()) {
            List<OutputProduct> outputProductList = outputProductRepository.findAllByOutput_Id(output_id);
            for (OutputProduct outputProduct : outputProductList) {
                Result result = new Result(true, outputProduct);
                results.add(result);
            }
            return results;
        }
        Result result = new Result(messageOutput.getMessage(), false);
        results.add(result);
        return results;
    }

    private Result addingOutputProduct(OutputProductDTO outputProductDTO) {
        OutputProduct outputProduct = new OutputProduct();

        Optional<Output> optionalOutput = outputRepository.findById(outputProductDTO.getOutput_id());
        if (!optionalOutput.isPresent()) {
            return new Result(messageOutput.getMessage(), false);
        }
        Output output = optionalOutput.get();

        Optional<Product> optionalProduct = productRepository.findById(outputProductDTO.getProduct_id());
        if (!optionalProduct.isPresent()) {
            return new Result(messageProduct.getMessage(), false);
        }
        Product product = optionalProduct.get();
        if (!product.isActive()) {
            return new Result(productActive.getMessageActive(), false);
        }

        outputProduct.setOutput(output);
        outputProduct.setProduct(product);
        outputProduct.setPrice(outputProductDTO.getPrice());
        outputProduct.setAmount(outputProductDTO.getAmount());
        return new Result(true, outputProduct);
    }

    public Result addOutputProduct(OutputProductDTO outputProductDTO) {
        Result result = addingOutputProduct(outputProductDTO);
        if (result.isSuccess()) {
            OutputProduct outputProduct = (OutputProduct) result.getObject();
            outputProductRepository.save(outputProduct);
            return new Result("Chiqim product saqlandi", true);
        }
        return result;
    }

    public Result editOutputProductById(Integer id, OutputProductDTO outputProductDTO) {
        Optional<OutputProduct> optionalOutputProduct = outputProductRepository.findById(id);
        if (optionalOutputProduct.isPresent()) {
            Result result = addingOutputProduct(outputProductDTO);
            if (result.isSuccess()) {
                OutputProduct editOutputProduct = optionalOutputProduct.get();
                OutputProduct outputProduct = (OutputProduct) result.getObject();
                editOutputProduct.setProduct(outputProduct.getProduct());
                editOutputProduct.setOutput(outputProduct.getOutput());
                editOutputProduct.setPrice(outputProduct.getPrice());
                editOutputProduct.setAmount(outputProduct.getAmount());
                outputProductRepository.save(editOutputProduct);
                return new Result("Chiqim product tahrirlandi", true);
            }
            return result;
        }
        return new Result(messageOutputProduct.getMessage(), false);
    }

    public Result deleteOutputProductById(Integer id) {
        Optional<OutputProduct> optionalOutputProduct = outputProductRepository.findById(id);
        if (optionalOutputProduct.isPresent()) {
            outputProductRepository.delete(optionalOutputProduct.get());
            return new Result("Chiqim product o'chirildi", true);
        }
        return new Result(messageOutputProduct.getMessage(), false);
    }
}
