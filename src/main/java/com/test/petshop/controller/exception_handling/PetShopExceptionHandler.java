package com.test.petshop.controller.exception_handling;

import com.test.petshop.dto.ApiResponse;
import com.test.petshop.exceptions.BadRequestException;
import com.test.petshop.exceptions.EntityNotFoundException;
import com.test.petshop.exceptions.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PetShopExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> entityNotFoundHandling(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .type(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> badRequestHandling(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse> invalidInputHandling(InvalidInputException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.builder()
                        .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                        .type(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                        .message(ex.getMessage())
                        .build());
    }

}
