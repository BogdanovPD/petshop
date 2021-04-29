package com.test.petshop.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(exclude = "pets")
public class Tag {

    @Id
    @GeneratedValue
    protected Integer id;
    @Column(unique = true, nullable = false)
    protected String name;
    @ManyToMany(mappedBy = "tags")
    private Set<Pet> pets = new HashSet<>();

}
