package com.example.datarestwarehouse.repository;

import com.example.datarestwarehouse.entity.InputProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputProductRepository extends JpaRepository<InputProduct,Integer> {
}
