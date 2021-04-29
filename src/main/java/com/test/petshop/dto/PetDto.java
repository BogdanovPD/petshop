package com.test.petshop.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "id")
public class PetDto {

    private Integer id;
    private String name;
    private String status;
    private String category;
    private List<String> photoUrls;
    private Set<String> tags;

}
