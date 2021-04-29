package com.test.petshop.service;

import com.test.petshop.dto.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public ApiResponse uploadImage(MultipartFile file, String metadata) {
        //upload to some service i.g. min.io
        String url = "https://service.img/" + UUID.randomUUID().toString();
        return ApiResponse.builder()
                .code(200)
                .type("url")
                .message(url)
                .build();
    }
}
