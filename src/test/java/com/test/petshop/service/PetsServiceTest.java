package com.test.petshop.service;

import com.test.petshop.dto.PetDto;
import com.test.petshop.exceptions.BadRequestException;
import com.test.petshop.exceptions.EntityNotFoundException;
import com.test.petshop.exceptions.InvalidInputException;
import com.test.petshop.repository.CategoryRepository;
import com.test.petshop.repository.PetsRepository;
import com.test.petshop.repository.TagsRepository;
import com.test.petshop.service.PetsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PetsServiceTest {

    @Autowired
    private PetsRepository petsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private PetsService petsService;

    @BeforeEach
    void init() {
        petsRepository.deleteAll();
        categoryRepository.deleteAll();
        tagsRepository.deleteAll();
    }

    @Test
    @DisplayName("Single element is added correctly")
    void addPet() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        assertThat(petDto, is(petDtoResp));
    }

    @Test
    @DisplayName("Several elements are added correctly")
    void addPetsWithDifferentData() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        PetDto petDto1 = getDifferentPetDto();
        PetDto petDtoResp1 = petsService.addPet(petDto1);
        assertThat(petDto, is(petDtoResp));
        assertThat(petDto1, is(petDtoResp1));
        assertThat(petsRepository.findAll().size(), is(2));
        assertThat(tagsRepository.findAll().size(), is(4));
        assertThat(categoryRepository.findAll().size(), is(2));
    }

    @Test
    @DisplayName("Several elements are added without children entities data duplication")
    void addPetsWithTheSameData() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        PetDto petDto1 = getPetDto();
        PetDto petDtoResp1 = petsService.addPet(petDto1);
        assertThat(petDto, is(petDtoResp));
        assertThat(petDto1, is(petDtoResp1));
        assertThat(petsRepository.findAll().size(), is(2));
        assertThat(tagsRepository.findAll().size(), is(2));
        assertThat(categoryRepository.findAll().size(), is(1));
    }

    @Test
    @DisplayName("Addition of single element with incorrect status throws exception")
    void addPetIncorrectStatus() {
        PetDto petDto = getPetDto();
        petDto.setStatus("Test");
        try {
            petsService.addPet(petDto);
        } catch (ConversionFailedException e) {
            assertThat(e.getCause(), instanceOf(InvalidInputException.class));
        }
    }

    @Test
    @DisplayName("Addition of single element with incorrect urls throws exception")
    void addPetIncorrectUrls() {
        PetDto petDto = getPetDto();
        petDto.setPhotoUrls(null);
        try {
            petsService.addPet(petDto);
        } catch (ConversionFailedException e) {
            assertThat(e.getCause(), instanceOf(InvalidInputException.class));
        }
    }

    @Test
    @DisplayName("Pet is deleted, children entity still in place")
    void deletePet() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        petsService.deletePet(petDtoResp.getId());
        assertThat(petsRepository.findAll(), empty());
        assertThat(tagsRepository.findAll().size(), is(2));
        assertThat(categoryRepository.findAll().size(), is(1));
    }

    @Test
    @DisplayName("Pet is updated correctly")
    void updatePet() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        petDtoResp.setName("newName");
        petDtoResp.setPhotoUrls(new ArrayList<>(List.of("newUrls"))); //just to make it mutable
        petDtoResp.setCategory("newCategory");
        petDtoResp.setTags(new HashSet<>(Set.of("newTag1", "newTag2")));
        PetDto petDtoResp1 = petsService.updatePet(petDtoResp);
        assertThat(petDtoResp, is(petDtoResp1));
    }

    @Test
    @DisplayName("Update throws exception on incorrect id")
    void updatePetWithIncorrectId() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        petDtoResp.setId(100);
        try {
            petsService.updatePet(petDtoResp);
        } catch (ConversionFailedException e) {
            assertThat(e.getCause(), instanceOf(EntityNotFoundException.class));
        }
    }

    @Test
    @DisplayName("Update throws exception on incorrect status")
    void updatePetWithIncorrectStatus() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        petDtoResp.setStatus("test");
        try {
            petsService.updatePet(petDtoResp);
        } catch (ConversionFailedException e) {
            assertThat(e.getCause(), instanceOf(InvalidInputException.class));
        }
    }

    @Test
    @DisplayName("Pet's name and status are updated correctly")
    void updatePetNameStatus() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        PetDto petDtoResp1 = petsService.updatePet(petDtoResp.getId(), "newName", "sold");
        assertThat(petDtoResp1.getName(), is("newName"));
        assertThat(petDtoResp1.getStatus(), is("sold"));
    }

    @Test
    @DisplayName("Pet's name and status update throws exception on incorrect id")
    void updatePetNameStatusWithIncorrectId() {
        PetDto petDto = getPetDto();
        petsService.addPet(petDto);
        assertThrows(EntityNotFoundException.class, () -> petsService.updatePet(100, "newName", "sold"));
    }

    @Test
    @DisplayName("Pet's name and status update throws exception on incorrect status")
    void updatePetNameStatusWithIncorrectStatus() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        assertThrows(BadRequestException.class, () -> petsService.updatePet(petDtoResp.getId(), "newName", "test"));
    }

    @Test
    @DisplayName("Get pet by id works correctly")
    void getPetById() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        PetDto petById = petsService.getPetById(petDtoResp.getId());
        assertThat(petById, is(petDtoResp));
    }

    @Test
    @DisplayName("Single element is added correctly")
    void getPetWithNonExistingId() {
        assertThrows(EntityNotFoundException.class, () -> petsService.getPetById(0));
    }

    @Test
    @DisplayName("Get pets by status works correctly")
    void getPetsByStatus() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        PetDto petDto1 = getDifferentPetDto();
        PetDto petDtoResp1 = petsService.addPet(petDto1);
        List<PetDto> petsByStatus = petsService.getPetsByStatus(List.of("sold", "available", "pending"));
        assertThat(petsByStatus, containsInAnyOrder(petDtoResp, petDtoResp1));
    }

    @Test
    @DisplayName("Get pets by status with incorrect status throws exception")
    void getPetsByStatusWithIncorrectStatus() {
        assertThrows(BadRequestException.class, () -> petsService.getPetsByStatus(List.of("sold", "available", "test")));
    }

    @Test
    @DisplayName("Get pets by tags works correctly")
    void getPetsByTags() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        PetDto petDto1 = getDifferentPetDto();
        PetDto petDtoResp1 = petsService.addPet(petDto1);
        List<PetDto> petsByTags = petsService.getPetsByTags(List.of("cat", "cat1"));
        assertThat(petsByTags, containsInAnyOrder(petDtoResp, petDtoResp1));
    }

    @Test
    @DisplayName("Get pets by incorrect tags works correctly")
    void getPetsByTagsWithNonExistingTags() {
        List<PetDto> petsByTags = petsService.getPetsByTags(List.of("test"));
        assertThat(petsByTags, empty());
    }

    @Test
    @DisplayName("Image uploads correctly")
    void uploadImage() {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = petsService.addPet(petDto);
        petsService.uploadImage(petDtoResp.getId(), null, null);
        petDtoResp = petsService.getPetById(petDtoResp.getId());
        assertThat(petDtoResp.getPhotoUrls().size(), is(2));
        assertThat(petDtoResp.getPhotoUrls().get(1), startsWith("https://service.img/"));
    }

    @Test
    @DisplayName("Image uploading throws exception on incorrect id")
    void uploadImageWithIncorrectId() {
        assertThrows(EntityNotFoundException.class, () -> petsService.uploadImage(100, null, null));
    }

    private PetDto getPetDto() {
        return PetDto.builder()
                .id(null)
                .category("test-category")
                .name("test-name")
                .photoUrls(List.of("test-url"))
                .status("available")
                .tags(Set.of("cat", "dog"))
                .build();
    }

    private PetDto getDifferentPetDto() {
        return PetDto.builder()
                .id(null)
                .category("test-category1")
                .name("test-name1")
                .photoUrls(List.of("test-url1"))
                .status("sold")
                .tags(Set.of("cat1", "dog1"))
                .build();
    }

}
