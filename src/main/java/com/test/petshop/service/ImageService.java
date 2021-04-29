package com.test.petshop.service;

import com.test.petshop.dto.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ApiResponse uploadImage(MultipartFile file, String metadata);
}
