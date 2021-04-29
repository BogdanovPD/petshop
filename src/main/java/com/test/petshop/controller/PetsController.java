package com.test.petshop.controller;

import com.test.petshop.dto.ApiResponse;
import com.test.petshop.dto.PetDto;
import com.test.petshop.service.PetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("pet")
public class PetsController {

    private final PetsService petsService;

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<PetDto> addPet(@RequestBody PetDto petDto) {
        return ResponseEntity.ok(petsService.addPet(petDto));
    }

    @PostMapping(path = "/{id}", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<PetDto> updatePet(@PathVariable Integer id,
                                            @RequestParam String name,
                                            @RequestParam String status) {
        return ResponseEntity.ok(petsService.updatePet(id, name, status));
    }

    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<PetDto> updatePet(@RequestBody PetDto petDto) {
        return ResponseEntity.ok(petsService.addPet(petDto));
    }

    @GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<PetDto> getPetById(@PathVariable Integer id) {
        return ResponseEntity.ok(petsService.getPetById(id));
    }

    @GetMapping(path = "/findByStatus", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<List<PetDto>> getPetsByStatus(@RequestParam List<String> status) {
        return ResponseEntity.ok(petsService.getPetsByStatus(status));
    }

    @Deprecated
    @GetMapping(path = "/findByTags", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<List<PetDto>> getPetsByTags(@RequestParam List<String> tags) {
        return ResponseEntity.ok(petsService.getPetsByTags(tags));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deletePetById(@PathVariable Integer id) {
        petsService.deletePet(id);
        return ApiResponse.builder()
                .code(200)
                .type("unknown")
                .message("1")
                .build();
    }

    @PostMapping(path = "/{id}/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse uploadImage(@PathVariable Integer id,
                                                   @RequestPart MultipartFile file,

                                                   @RequestPart String metadata) {
        return petsService.uploadImage(id, file, metadata);
    }

}
