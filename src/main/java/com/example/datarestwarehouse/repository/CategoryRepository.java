package com.example.datarestwarehouse.repository;

import com.example.datarestwarehouse.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    boolean existsByParentCategory_Id(Integer parentCategory_id);

    List<Category> getAllByParentCategory_Id(Integer parentCategory_id);

    boolean existsByName(String name);

    boolean existsByIdIsNotAndName(Integer id, String name);

    Category getByParentCategory_Id(Integer parentCategory_id);
}
