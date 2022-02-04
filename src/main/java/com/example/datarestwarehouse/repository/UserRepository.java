package com.example.datarestwarehouse.repository;

import com.example.datarestwarehouse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByPassword(String password);

    boolean existsByPhoneNumber(String phoneNumber);
}
