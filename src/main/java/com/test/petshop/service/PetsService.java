package com.test.petshop.service;

import com.test.petshop.dto.ApiResponse;
import com.test.petshop.dto.PetDto;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PetsService {
    PetDto getPetById(Integer id);

    List<PetDto> getPetsByStatus(List<String> statusList);

    @Deprecated
    List<PetDto> getPetsByTags(List<String> tagsList);

    PetDto addPet(PetDto petDto);

    PetDto updatePet(PetDto petDto);

    PetDto updatePet(Integer id, String name, String status);

    void deletePet(Integer id);

    ApiResponse uploadImage(Integer id, MultipartFile file, String metadata);
}
