package com.example.datarestwarehouse.repository;

import com.example.datarestwarehouse.entity.OutputProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutputProductRepository extends JpaRepository<OutputProduct,Integer> {

    List<OutputProduct> findAllByOutput_Id(Integer output_id);
}
