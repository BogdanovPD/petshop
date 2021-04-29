package com.test.petshop.repository;

import com.test.petshop.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TagsRepository extends JpaRepository<Tag, Integer> {

    Set<Tag> findAllByNameIn(Set<String> names);

}
