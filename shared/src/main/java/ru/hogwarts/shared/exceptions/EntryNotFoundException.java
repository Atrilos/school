package ru.hogwarts.shared.exceptions;

public class EntryNotFoundException extends RuntimeException {
    public EntryNotFoundException() {
        super();
    }

    public EntryNotFoundException(String message) {
        super(message);
    }
}
