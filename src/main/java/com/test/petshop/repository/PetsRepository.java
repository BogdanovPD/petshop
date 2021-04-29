package com.test.petshop.repository;

import com.test.petshop.model.Pet;
import com.test.petshop.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetsRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findAllByStatusIn(List<Status> statuses);
    @Query("select distinct p from Pet p join p.tags t where t.name in :tags")
    List<Pet> findAllByTags(List<String> tags);

}
