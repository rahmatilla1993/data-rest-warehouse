package com.example.datarestwarehouse.service;

import com.example.datarestwarehouse.entity.Currency;
import com.example.datarestwarehouse.entity.Input;
import com.example.datarestwarehouse.entity.Supplier;
import com.example.datarestwarehouse.entity.Warehouse;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.InputDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.repository.CurrencyRepository;
import com.example.datarestwarehouse.repository.InputRepository;
import com.example.datarestwarehouse.repository.SupplierRepository;
import com.example.datarestwarehouse.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.instrument.classloading.weblogic.WebLogicLoadTimeWeaver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InputService {

    ElementNotFound messageInput = ElementNotFound.INPUT;
    ElementNotFound messageWarehouse = ElementNotFound.WAREHOUSE;
    ElementNotFound messageSupplier = ElementNotFound.SUPPLIER;
    ElementNotFound messageCurrency = ElementNotFound.CURRENCY;

    ElementIsActive warehouseActive = ElementIsActive.WAREHOUSE;
    ElementIsActive supplierActive = ElementIsActive.SUPPLIER;
    ElementIsActive currencyActive = ElementIsActive.CURRENCY;

    @Autowired
    InputRepository inputRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    public List<Input> getAllInputs() {
        return inputRepository.findAll();
    }

    public Result getInputById(Integer id) {
        Optional<Input> optionalInput = inputRepository.findById(id);
        return optionalInput.map(input -> new Result(true, input)).orElseGet(() -> new Result(messageInput.getMessage(), false));
    }

    private String generateCode() {
        int size = inputRepository.findAll().size();
        if (size == 0) {
            return "1";
        }
        Input input = inputRepository.findAll().get(size - 1);
        size = input.getId() + 1;
        return Integer.toString(size);
    }

    private Result addingInput(InputDTO inputDTO, boolean create, boolean edit, Integer id) {
        Input input = new Input();

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(inputDTO.getWarehouse_id());
        if (!optionalWarehouse.isPresent()) {
            return new Result(messageWarehouse.getMessage(), false);
        }
        Warehouse warehouse = optionalWarehouse.get();
        if (!warehouse.isActive()) {
            return new Result(warehouseActive.getMessageActive(), false);
        }

        Optional<Supplier> optionalSupplier = supplierRepository.findById(inputDTO.getSupplier_id());
        if (!optionalSupplier.isPresent()) {
            return new Result(messageSupplier.getMessage(), false);
        }
        Supplier supplier = optionalSupplier.get();
        if (!supplier.isActive()) {
            return new Result(supplierActive.getMessageActive(), false);
        }

        Optional<Currency> optionalCurrency = currencyRepository.findById(inputDTO.getCurrency_id());
        if (!optionalCurrency.isPresent()) {
            return new Result(messageCurrency.getMessage(), false);
        }
        Currency currency = optionalCurrency.get();
        if (!currency.isActive()) {
            return new Result(currencyActive.getMessageActive(), false);
        }

        if (create && inputRepository.existsByFactureNumber(inputDTO.getFactureNumber()) ||
                edit && inputRepository.existsByIdIsNotAndFactureNumber(id, inputDTO.getFactureNumber())) {
            return new Result("Bunday facture number bor", false);
        }

        input.setCurrency(currency);
        input.setDate(inputDTO.getDate());
        input.setFactureNumber(inputDTO.getFactureNumber());
        input.setSupplier(supplier);
        input.setWarehouse(warehouse);
        return new Result(true, input);
    }

    public Result addInput(InputDTO inputDTO) {
        Result result = addingInput(inputDTO, true, false, null);
        if (result.isSuccess()) {
            Input input = (Input) result.getObject();
            input.setCode(generateCode());
            inputRepository.save(input);
            return new Result("Kirim qo'shildi", true);
        }
        return result;
    }

    public Result deleteInputById(Integer id) {
        Optional<Input> optionalInput = inputRepository.findById(id);
        if (optionalInput.isPresent()) {
            inputRepository.delete(optionalInput.get());
            return new Result("Input o'chirildi", true);
        }
        return new Result(messageInput.getMessage(), false);
    }

    public Result editInputById(Integer id, InputDTO inputDTO) {
        Optional<Input> optionalInput = inputRepository.findById(id);
        if (optionalInput.isPresent()) {
            Result result = addingInput(inputDTO, false, true, id);
            if (result.isSuccess()) {
                Input editInput = optionalInput.get();
                Input input = (Input) result.getObject();
                editInput.setWarehouse(input.getWarehouse());
                editInput.setSupplier(input.getSupplier());
                editInput.setFactureNumber(input.getFactureNumber());
                editInput.setCurrency(input.getCurrency());
                editInput.setDate(input.getDate());
                return new Result("Input tahrirlandi", true);
            }
            return result;
        }
        return new Result(messageInput.getMessage(), false);
    }
}
