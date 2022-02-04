package com.example.datarestwarehouse.controller;

import com.example.datarestwarehouse.entity.Input;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.InputDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.service.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/input")
public class InputController {

    ElementNotFound messageInput = ElementNotFound.INPUT;
    ElementNotFound messageWarehouse = ElementNotFound.WAREHOUSE;
    ElementNotFound messageSupplier = ElementNotFound.SUPPLIER;
    ElementNotFound messageCurrency = ElementNotFound.CURRENCY;

    ElementIsActive warehouseActive = ElementIsActive.WAREHOUSE;
    ElementIsActive supplierActive = ElementIsActive.SUPPLIER;
    ElementIsActive currencyActive = ElementIsActive.CURRENCY;

    @Autowired
    InputService inputService;

    @GetMapping
    public HttpEntity<?> getAllInputs() {
        List<Input> allInputs = inputService.getAllInputs();
        return ResponseEntity.ok(allInputs);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getInputById(@PathVariable Integer id) {
        Result result = inputService.getInputById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping
    public HttpEntity<?> addInput(@Valid @RequestBody InputDTO inputDTO) {
        Result result = inputService.addInput(inputDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : result.getMessage().equals(messageWarehouse.getMessage()) ||
                result.getMessage().equals(messageSupplier.getMessage()) || result.getMessage().equals(messageCurrency.getMessage()) ?
                HttpStatus.NOT_FOUND : result.getMessage().equals(warehouseActive.getMessageActive()) || result.getMessage().equals(currencyActive.getMessageActive()) ||
                result.getMessage().equals(supplierActive.getMessageActive()) ? HttpStatus.FORBIDDEN : HttpStatus.CONFLICT).body(result);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteInputById(@PathVariable Integer id) {
        Result result = inputService.deleteInputById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND).body(result);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editInput(@PathVariable Integer id, @Valid @RequestBody InputDTO inputDTO) {
        Result result = inputService.editInputById(id, inputDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : result.getMessage().equals(messageWarehouse.getMessage()) ||
                result.getMessage().equals(messageSupplier.getMessage()) || result.getMessage().equals(messageCurrency.getMessage()) ?
                HttpStatus.NOT_FOUND : result.getMessage().equals(warehouseActive.getMessageActive()) || result.getMessage().equals(currencyActive.getMessageActive()) ||
                result.getMessage().equals(supplierActive.getMessageActive()) ? HttpStatus.FORBIDDEN : HttpStatus.CONFLICT).body(result);
    }
}
