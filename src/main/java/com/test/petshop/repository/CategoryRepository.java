package com.test.petshop.repository;

import com.test.petshop.model.Category;
import com.test.petshop.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

}
