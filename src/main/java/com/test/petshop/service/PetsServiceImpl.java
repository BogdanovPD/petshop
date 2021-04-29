package com.test.petshop.service;

import com.test.petshop.dto.ApiResponse;
import com.test.petshop.dto.PetDto;
import com.test.petshop.exceptions.BadRequestException;
import com.test.petshop.exceptions.EntityNotFoundException;
import com.test.petshop.model.Pet;
import com.test.petshop.model.enums.Status;
import com.test.petshop.repository.PetsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PetsServiceImpl implements PetsService {

    private final ConversionService petConverterService;
    private final PetsRepository petsRepository;
    private final ImageService imageService;

    @Override
    public PetDto getPetById(Integer id) {
        return petConverterService.convert(
                petsRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Pet not found")),
                PetDto.class);
    }

    @Override
    public List<PetDto> getPetsByStatus(List<String> statusList) {
        try {
            return petsRepository.findAllByStatusIn(
                    statusList.stream()
                            .map(Status::valueOf).collect(Collectors.toList())).stream()
                    .map(pet -> petConverterService.convert(pet, PetDto.class))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status value");
        }
    }

    @Override
    public List<PetDto> getPetsByTags(List<String> tagsList) {
        return petsRepository.findAllByTags(tagsList).stream()
                .map(pet -> petConverterService.convert(pet, PetDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PetDto addPet(PetDto petDto) {
        petDto.setId(null);
        Pet pet = petConverterService.convert(petDto, Pet.class);
        petsRepository.save(pet);
        return petConverterService.convert(pet, PetDto.class);
    }

    @Override
    public PetDto updatePet(PetDto petDto) {
        Pet pet = petConverterService.convert(petDto, Pet.class);
        pet = petsRepository.save(pet);
        return petConverterService.convert(pet, PetDto.class);
    }

    @Override
    public PetDto updatePet(Integer id, String name, String status) {
        try {
            Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status value");
        }
        Pet pet = petsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        pet.setName(name);
        pet.setStatus(Status.valueOf(status));
        pet = petsRepository.save(pet);
        return petConverterService.convert(pet, PetDto.class);
    }

    @Override
    public void deletePet(Integer id) {
        Pet pet = petsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        petsRepository.delete(pet);
    }

    @Override
    public ApiResponse uploadImage(Integer id, MultipartFile file, String metadata) {
        Pet pet = petsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        ApiResponse apiResponse = imageService.uploadImage(file, metadata);
        if (apiResponse.getCode() == 200 && apiResponse.getType().equals("url")) {
            pet.getPhotoUrls().add(apiResponse.getMessage());
            petsRepository.save(pet);
        }
        return apiResponse;
    }


}
