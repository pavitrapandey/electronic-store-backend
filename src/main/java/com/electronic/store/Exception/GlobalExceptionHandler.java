package com.electronic.store.Exception;

import com.electronic.store.dtos.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponseMessage resourceNotFoundException(ResourceNotFoundException ex){
        return ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

     @ExceptionHandler(EmailAlreadyExistException.class)
    public ApiResponseMessage emailAlreadyExistException(EmailAlreadyExistException ex) {
         return ApiResponseMessage.builder()
                 .message(ex.getMessage())
                 .success(true)
                 .status(HttpStatus.BAD_REQUEST)
                 .build();
     }

    //MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ObjectError> objectErrorList= ex.getBindingResult().getAllErrors();
        Map<String, Object> response= new HashMap<>();
        objectErrorList.stream().forEach(objectError-> {
            String message= objectError.getDefaultMessage();
            String fieldName= ((FieldError)objectError).getField();
            response.put(fieldName,message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //handle BadRequestException
    @ExceptionHandler(BadRequestApiException.class)
    public ResponseEntity<ApiResponseMessage> handleBadRequestException(BadRequestApiException ex){
        ApiResponseMessage response= ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //CategoryAlreadyExistException
    @ExceptionHandler(CategoryAlreadyExistException.class)
    public ResponseEntity<ApiResponseMessage> handleCategoryAlreadyExistException(CategoryAlreadyExistException ex){
        ApiResponseMessage response= ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
