package com.example.datarestwarehouse.repository;

import com.example.datarestwarehouse.entity.Output;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutputRepository extends JpaRepository<Output,Integer> {

    boolean existsByFactureNumber(String factureNumber);

    boolean existsByIdIsNotAndFactureNumber(Integer id, String factureNumber);
}
