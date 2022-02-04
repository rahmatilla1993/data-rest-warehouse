package com.example.datarestwarehouse.repository;

import com.example.datarestwarehouse.entity.Input;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputRepository extends JpaRepository<Input,Integer> {

    boolean existsByFactureNumber(String factureNumber);

    boolean existsByIdIsNotAndFactureNumber(Integer id, String factureNumber);
}
