package com.example.datarestwarehouse.controller;

import com.example.datarestwarehouse.entity.Output;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.OutputDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.service.OutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/output")
public class OutputController {

    @Autowired
    OutputService outputService;

    ElementNotFound messageOutput = ElementNotFound.OUTPUT;
    ElementNotFound messageWarehouse = ElementNotFound.WAREHOUSE;
    ElementNotFound messageCurrency = ElementNotFound.CURRENCY;
    ElementNotFound messageClient = ElementNotFound.CLIENT;

    ElementIsActive wareHouseActive = ElementIsActive.WAREHOUSE;
    ElementIsActive currencyActive = ElementIsActive.CURRENCY;

    @GetMapping
    public HttpEntity<?> getAllOutputs(int page) {
        Page<Output> allOutputs = outputService.getAllOutputs(page);
        return ResponseEntity.ok(allOutputs);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOutputById(@PathVariable Integer id) {
        Result result = outputService.getOutputById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping
    public HttpEntity<?> addOutput(@Valid @RequestBody OutputDTO outputDTO) {
        Result result = outputService.addOutput(outputDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : result.getMessage().equals(messageWarehouse.getMessage()) ||
                result.getMessage().equals(messageClient.getMessage()) || result.getMessage().equals(messageCurrency.getMessage()) ?
                HttpStatus.NOT_FOUND : result.getMessage().equals(wareHouseActive.getMessageActive()) || result.getMessage().equals(currencyActive.getMessageActive()) ?
                HttpStatus.FORBIDDEN : HttpStatus.CONFLICT).body(result);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editOutputById(@PathVariable Integer id, @Valid @RequestBody OutputDTO outputDTO) {
        Result result = outputService.editOutputById(id, outputDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : result.getMessage().equals(messageWarehouse.getMessage()) ||
                result.getMessage().equals(messageClient.getMessage()) || result.getMessage().equals(messageCurrency.getMessage()) ||
                result.getMessage().equals(messageOutput.getMessage()) ? HttpStatus.NOT_FOUND : result.getMessage().equals(wareHouseActive.getMessageActive())
                || result.getMessage().equals(currencyActive.getMessageActive()) ? HttpStatus.FORBIDDEN : HttpStatus.CONFLICT).body(result);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteOutputById(@PathVariable Integer id) {
        Result result = outputService.deleteOutputById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND).body(result);
    }
}
