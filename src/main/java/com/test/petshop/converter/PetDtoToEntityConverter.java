package com.test.petshop.converter;

import com.test.petshop.dto.PetDto;
import com.test.petshop.exceptions.EntityNotFoundException;
import com.test.petshop.exceptions.InvalidInputException;
import com.test.petshop.model.Category;
import com.test.petshop.model.Pet;
import com.test.petshop.model.Tag;
import com.test.petshop.model.enums.Status;
import com.test.petshop.repository.CategoryRepository;
import com.test.petshop.repository.PetsRepository;
import com.test.petshop.repository.TagsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PetDtoToEntityConverter implements Converter<PetDto, Pet> {

    private final PetsRepository petsRepository;
    private final CategoryRepository categoryRepository;
    private final TagsRepository tagsRepository;

    @Override
    public Pet convert(PetDto petDto) {
        Pet pet;
        if (petDto.getId() == null) {
            pet = new Pet();
        } else {
            pet = petsRepository.findById(petDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        }
        pet.setName(petDto.getName());
        Category category = null;
        if (!StringUtils.isBlank(petDto.getCategory())) {
            category = categoryRepository.findByName(petDto.getCategory()).orElse(Category.builder().name(petDto.getCategory()).build());
        }
        pet.setCategory(category);
        if (petDto.getPhotoUrls() == null || petDto.getPhotoUrls().isEmpty()) {
            throw new InvalidInputException("Photo urls must not be empty or null");
        }
        pet.setPhotoUrls(petDto.getPhotoUrls());
        Status status = null;
        if (!StringUtils.isBlank(petDto.getStatus())) {
            try {
                status = Status.valueOf(petDto.getStatus());
            } catch (IllegalArgumentException ex) {
                throw new InvalidInputException("Status is not found");
            }
        }
        pet.setStatus(status);
        Set<Tag> tags = new HashSet<>();
        if (petDto.getTags() != null && !petDto.getTags().isEmpty()) {
            tags = getTags(petDto);
        }
        pet.setTags(tags);
        return pet;
    }

    private Set<Tag> getTags(PetDto petDto) {
        Set<Tag> tags = tagsRepository.findAllByNameIn(petDto.getTags());
        if (tags.size() < petDto.getTags().size()) {
            List<Tag> newTags = new ArrayList<>();
            List<String> dbTags = tags.stream().map(Tag::getName).collect(Collectors.toList());
            petDto.getTags().forEach(name -> {
                if (!dbTags.contains(name)) {
                    newTags.add(Tag.builder().name(name).build());
                }
            });
            tags.addAll(newTags);
        }
        return tags;
    }
}
