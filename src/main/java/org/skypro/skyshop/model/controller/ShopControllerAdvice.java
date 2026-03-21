package org.skypro.skyshop.model.controller;

import org.skypro.skyshop.model.error.ShopError;
import org.skypro.skyshop.model.exceptions.NoSuchProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ShopControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ShopControllerAdvice.class);

    @ExceptionHandler(NoSuchProductException.class)
    public ResponseEntity<ShopError> handleNoSuchProductException(NoSuchProductException ex) {
        logger.warn("Product not found: {}", ex.getMessage());
        ShopError error = new ShopError(
                "PRODUCT_NOT_FOUND",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ShopError> handleNoResourceFoundException(NoResourceFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        ShopError error = new ShopError(
                "RESOURCE_NOT_FOUND",
                "Запрашиваемый ресурс не найден"
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ShopError> handleGenericException(Exception ex) {
        logger.error("Internal server error", ex);

        if (ex.getMessage() != null && ex.getMessage().contains("No static resource")) {
            ShopError error = new ShopError(
                    "RESOURCE_NOT_FOUND",
                    "Запрашиваемый ресурс не найден"
            );
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        ShopError error = new ShopError(
                "INTERNAL_SERVER_ERROR",
                "Произошла внутренняя ошибка сервера"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}