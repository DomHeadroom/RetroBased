package com.retrobased.market.utils.handlers;

import com.retrobased.market.utils.exceptions.ArgumentValueNotValidException;
import com.retrobased.market.utils.exceptions.AttributeNotFoundException;
import com.retrobased.market.utils.exceptions.CategoryNotFoundException;
import com.retrobased.market.utils.exceptions.CountryNotFoundException;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import com.retrobased.market.utils.exceptions.ProductNotFoundException;
import com.retrobased.market.utils.exceptions.SellerNotFoundException;
import com.retrobased.market.utils.exceptions.TagNotFoundException;
import com.retrobased.market.utils.exceptions.UserSaveException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.retrobased.market.utils.handlers.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(ArgumentValueNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(ArgumentValueNotValidException exp) {
        return ResponseEntity
                .status(VALUE_NOT_PERMITTED.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(VALUE_NOT_PERMITTED.getCode())
                                .businessErrorDescription(VALUE_NOT_PERMITTED.getDescription())
                                .error(VALUE_NOT_PERMITTED.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(ProductNotFoundException exp) {
        return ResponseEntity
                .status(PRODUCT_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(PRODUCT_NOT_FOUND.getCode())
                                .businessErrorDescription(PRODUCT_NOT_FOUND.getDescription())
                                .error(PRODUCT_NOT_FOUND.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(CustomerNotFoundException exp) {
        return ResponseEntity
                .status(CUSTOMER_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(CUSTOMER_NOT_FOUND.getCode())
                                .businessErrorDescription(CUSTOMER_NOT_FOUND.getDescription())
                                .error(CUSTOMER_NOT_FOUND.getDescription())
                                .build()
                );
    }


    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(CountryNotFoundException exp) {
        return ResponseEntity
                .status(COUNTRY_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(COUNTRY_NOT_FOUND.getCode())
                                .businessErrorDescription(COUNTRY_NOT_FOUND.getDescription())
                                .error(COUNTRY_NOT_FOUND.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(SellerNotFoundException exp) {
        return ResponseEntity
                .status(SELLER_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(SELLER_NOT_FOUND.getCode())
                                .businessErrorDescription(SELLER_NOT_FOUND.getDescription())
                                .error(SELLER_NOT_FOUND.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(AttributeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(AttributeNotFoundException exp) {
        return ResponseEntity
                .status(ATTRIBUTE_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ATTRIBUTE_NOT_FOUND.getCode())
                                .businessErrorDescription(ATTRIBUTE_NOT_FOUND.getDescription())
                                .error(ATTRIBUTE_NOT_FOUND.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(TagNotFoundException exp) {
        return ResponseEntity
                .status(TAG_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(TAG_NOT_FOUND.getCode())
                                .businessErrorDescription(TAG_NOT_FOUND.getDescription())
                                .error(TAG_NOT_FOUND.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(CategoryNotFoundException exp) {
        return ResponseEntity
                .status(CATEGORY_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(CATEGORY_NOT_FOUND.getCode())
                                .businessErrorDescription(CATEGORY_NOT_FOUND.getDescription())
                                .error(CATEGORY_NOT_FOUND.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(UserSaveException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserSaveException exp) {
        return ResponseEntity
                .status(USER_CREATION_FAILED.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(USER_CREATION_FAILED.getCode())
                                .businessErrorDescription(USER_CREATION_FAILED.getDescription())
                                .error(USER_CREATION_FAILED.getDescription())
                                .build()
                );
    }


}
