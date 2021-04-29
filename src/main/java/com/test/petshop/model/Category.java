package com.test.petshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {

    @Id
    @GeneratedValue
    protected Integer id;
    @Column(unique = true, nullable = false)
    protected String name;

}
