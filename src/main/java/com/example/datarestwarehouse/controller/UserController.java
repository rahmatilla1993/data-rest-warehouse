package com.example.datarestwarehouse.controller;

import com.example.datarestwarehouse.entity.User;
import com.example.datarestwarehouse.enums.ElementIsActive;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.models.UserDTO;
import com.example.datarestwarehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    ElementIsActive warehouseActive = ElementIsActive.WAREHOUSE;
    ElementNotFound messageWarehouse = ElementNotFound.WAREHOUSE;
    ElementNotFound messageUser = ElementNotFound.USER;

    @Autowired
    UserService userService;

    @GetMapping
    public HttpEntity<?> getAllUsers(@RequestParam int page) {
        Page<User> allUsers = userService.getAllUsers(page);
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getUserById(@PathVariable Integer id) {
        Result result = userService.getUserById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping
    public HttpEntity<?> addUser(@Valid @RequestBody UserDTO userDTO) {
        Result result = userService.addUser(userDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : result.getMessage().equals(warehouseActive.getMessageActive()) ?
                HttpStatus.FORBIDDEN : result.getMessage().equals(messageWarehouse.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT).body(result);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editUserById(@PathVariable Integer id, @Valid @RequestBody UserDTO userDTO) {
        Result result = userService.editUserById(id, userDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : result.getMessage().equals(messageWarehouse.getMessage()) ||
                result.getMessage().equals(messageUser.getMessage()) ? HttpStatus.NOT_FOUND : result.getMessage().equals(warehouseActive.getMessageActive()) ?
                HttpStatus.FORBIDDEN : HttpStatus.CONFLICT).body(result);
    }
}
