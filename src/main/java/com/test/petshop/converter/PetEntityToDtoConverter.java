package com.test.petshop.converter;

import com.test.petshop.dto.PetDto;
import com.test.petshop.exceptions.EntityNotFoundException;
import com.test.petshop.model.Category;
import com.test.petshop.model.Pet;
import com.test.petshop.model.Tag;
import com.test.petshop.repository.CategoryRepository;
import com.test.petshop.repository.PetsRepository;
import com.test.petshop.repository.TagsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PetEntityToDtoConverter implements Converter<Pet, PetDto> {

    @Override
    public PetDto convert(Pet pet) {
        return PetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .category(pet.getCategory() == null ? "" : pet.getCategory().getName())
                .status(pet.getStatus().name())
                .photoUrls(new ArrayList<>(pet.getPhotoUrls()))
                .tags(pet.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .build();
    }
}
