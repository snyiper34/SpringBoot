package org.skypro.skyshop.model.exceptions;

import java.util.UUID;

public class NoSuchProductException extends RuntimeException {

    public NoSuchProductException(String message) {
        super(message);
    }

    public NoSuchProductException(UUID id) {
        super("Товар с id " + id + " не найден");
    }

    public NoSuchProductException() {
        super("Товар не найден");
    }
}