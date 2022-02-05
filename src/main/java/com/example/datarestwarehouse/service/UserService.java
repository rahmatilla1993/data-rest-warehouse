package com.example.datarestwarehouse.service;

import com.example.datarestwarehouse.entity.User;
import com.example.datarestwarehouse.entity.Warehouse;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.models.UserDTO;
import com.example.datarestwarehouse.repository.UserRepository;
import com.example.datarestwarehouse.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    ElementNotFound messageUser = ElementNotFound.USER;
    ElementNotFound messageWarehouse = ElementNotFound.WAREHOUSE;

    ElementIsActive warehouseActive = ElementIsActive.WAREHOUSE;

    public Page<User> getAllUsers(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return userRepository.findAll(pageable);
    }

    public Result getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> new Result(true, user)).orElseGet(() -> new Result(messageUser.getMessage(), false));
    }

    private String generateUserCode() {
        int size = userRepository.findAll().size();
        if (size == 0) {
            return "1";
        }
        User user = userRepository.findAll().get(size - 1);
        return user.getId().toString();
    }

    private Result addingUser(UserDTO userDTO, boolean create, boolean edit, Integer id) {
        User user = new User();
        if (create && userRepository.existsByPhoneNumber(userDTO.getPhoneNumber()) ||
                edit && userRepository.existsByIdIsNotAndPhoneNumber(id, userDTO.getPhoneNumber())) {
            return new Result("Bunday telefon nomeri bor", false);
        }
        if (create && userRepository.existsByPassword(userDTO.getPassword()) ||
                edit && userRepository.existsByIdIsNotAndPassword(id, userDTO.getPassword())) {
            return new Result("Bunday parol bor", false);
        }

        Set<Warehouse> warehouseSet = new HashSet<>();
        Set<Integer> warehouses = userDTO.getWarehouses();
        for (Integer warehouse_id : warehouses) {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(warehouse_id);
            if (optionalWarehouse.isPresent()) {
                Warehouse warehouse = optionalWarehouse.get();
                if (warehouse.isActive()) {
                    warehouseSet.add(warehouse);
                } else
                    return new Result(warehouseActive.getMessageActive(), false);
            } else
                return new Result(messageWarehouse.getMessage(), false);
        }
        user.setActive(userDTO.isActive());
        user.setFirstName(userDTO.getFirstName());
        user.setPassword(userDTO.getPassword());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setWarehouses(warehouseSet);
        return new Result(true, user);
    }

    public Result addUser(UserDTO userDTO) {
        Result result = addingUser(userDTO, true, false, null);
        if (result.isSuccess()) {
            User user = (User) result.getObject();
            user.setCode(generateUserCode());
            userRepository.save(user);
            return new Result("User saqlandi", true);
        }
        return result;
    }

    public Result editUserById(Integer id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            Result result = addingUser(userDTO, false, true, id);
            if (result.isSuccess()) {
                User editUser = optionalUser.get();
                User user = new User();
                editUser.setPhoneNumber(user.getPhoneNumber());
                editUser.setPassword(user.getPassword());
                editUser.setActive(user.isActive());
                editUser.setLastName(user.getLastName());
                editUser.setFirstName(user.getFirstName());
                editUser.setWarehouses(user.getWarehouses());
                return new Result("User o'zgartirildi", true);
            }
            return result;
        }
        return new Result(messageUser.getMessage(), false);
    }
}
