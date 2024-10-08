package com.retrobased.market.utils.handlers;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),

    VALUE_NOT_PERMITTED(422, UNPROCESSABLE_ENTITY, "Value is not permitted"),
    PRODUCT_NOT_FOUND(300, NOT_FOUND, "Product not found"),
    CUSTOMER_NOT_FOUND(301, NOT_FOUND, "Customer not found"),
    COUNTRY_NOT_FOUND(302, NOT_FOUND, "Country not found"),
    ADDRESS_NOT_FOUND(303, NOT_FOUND, "Address not found"),
    SELLER_NOT_FOUND(304, NOT_FOUND, "Seller not found"),
    ATTRIBUTE_NOT_FOUND(305, NOT_FOUND, "Attribute not found"),
    CATEGORY_NOT_FOUND(306, NOT_FOUND, "Category not found"),
    TAG_NOT_FOUND(307, NOT_FOUND, "Tag not found"),


    ;

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}
