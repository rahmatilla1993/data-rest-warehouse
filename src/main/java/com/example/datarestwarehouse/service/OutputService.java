package com.example.datarestwarehouse.service;

import com.example.datarestwarehouse.entity.Client;
import com.example.datarestwarehouse.entity.Currency;
import com.example.datarestwarehouse.entity.Output;
import com.example.datarestwarehouse.entity.Warehouse;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.OutputDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.repository.ClientRepository;
import com.example.datarestwarehouse.repository.CurrencyRepository;
import com.example.datarestwarehouse.repository.OutputRepository;
import com.example.datarestwarehouse.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OutputService {

    ElementNotFound messageOutput = ElementNotFound.OUTPUT;
    ElementNotFound messageWarehouse = ElementNotFound.WAREHOUSE;
    ElementNotFound messageCurrency = ElementNotFound.CURRENCY;
    ElementNotFound messageClient = ElementNotFound.CLIENT;

    ElementIsActive wareHouseActive = ElementIsActive.WAREHOUSE;
    ElementIsActive currencyActive = ElementIsActive.CURRENCY;

    @Autowired
    OutputRepository outputRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    ClientRepository clientRepository;

    public Page<Output> getAllOutputs(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return outputRepository.findAll(pageable);
    }

    public Result getOutputById(Integer id) {
        Optional<Output> optionalOutput = outputRepository.findById(id);
        return optionalOutput.map(output -> new Result(true, output)).orElseGet(() -> new Result(messageOutput.getMessage(), false));
    }

    private String generateOutputCode() {
        int size = outputRepository.findAll().size();
        if (size == 0) {
            return "1";
        }
        Output output = outputRepository.findAll().get(size - 1);
        return output.getId().toString();
    }

    private Result addingOutput(OutputDTO outputDTO, boolean create, boolean edit, Integer id) {
        Output output = new Output();

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(outputDTO.getWarehouse_id());
        if (!optionalWarehouse.isPresent()) {
            return new Result(messageWarehouse.getMessage(), false);
        }
        Warehouse warehouse = optionalWarehouse.get();
        if (!warehouse.isActive()) {
            return new Result(wareHouseActive.getMessageActive(), false);
        }

        Optional<Currency> optionalCurrency = currencyRepository.findById(outputDTO.getCurrency_id());
        if (!optionalCurrency.isPresent()) {
            return new Result(messageCurrency.getMessage(), false);
        }
        Currency currency = optionalCurrency.get();
        if (!currency.isActive()) {
            return new Result(currencyActive.getMessageActive(), false);
        }

        Optional<Client> optionalClient = clientRepository.findById(outputDTO.getClient_id());
        if (!optionalClient.isPresent()) {
            return new Result(messageClient.getMessage(), false);
        }
        Client client = optionalClient.get();

        if (create && outputRepository.existsByFactureNumber(outputDTO.getFactureNumber()) ||
                edit && outputRepository.existsByIdIsNotAndFactureNumber(id, outputDTO.getFactureNumber())) {
            return new Result("Bunday factura raqami bor", false);
        }

        output.setClient(client);
        output.setDate(outputDTO.getDate());
        output.setWarehouse(warehouse);
        output.setCurrency(currency);
        output.setFactureNumber(outputDTO.getFactureNumber());
        return new Result(true, output);
    }

    public Result addOutput(OutputDTO outputDTO) {
        Result result = addingOutput(outputDTO, true, false, null);
        if (result.isSuccess()) {
            Output output = (Output) result.getObject();
            output.setCode(generateOutputCode());
            outputRepository.save(output);
            return new Result("Chiqim saqlandi", true);
        }
        return result;
    }

    public Result editOutputById(Integer id, OutputDTO outputDTO) {
        Optional<Output> optionalOutput = outputRepository.findById(id);
        if (optionalOutput.isPresent()) {
            Result result = addingOutput(outputDTO, false, true, id);
            if (result.isSuccess()) {
                Output editOutput = optionalOutput.get();
                Output output = (Output) result.getObject();
                editOutput.setCurrency(output.getCurrency());
                editOutput.setWarehouse(output.getWarehouse());
                editOutput.setDate(output.getDate());
                editOutput.setFactureNumber(output.getFactureNumber());
                editOutput.setClient(output.getClient());
                outputRepository.save(editOutput);
                return new Result("Chiqim o'zgartirildi", true);
            }
            return result;
        }
        return new Result(messageOutput.getMessage(), false);
    }

    public Result deleteOutputById(Integer id) {
        Optional<Output> optionalOutput = outputRepository.findById(id);
        if (optionalOutput.isPresent()) {
            outputRepository.delete(optionalOutput.get());
            return new Result("Chiqim o'chirildi", true);
        }
        return new Result(messageOutput.getMessage(), false);
    }
}
