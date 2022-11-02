package ru.hogwarts.school.exceptions;

public class EntryNotFoundException extends RuntimeException {
    public EntryNotFoundException() {
        super();
    }

    public EntryNotFoundException(String message) {
        super(message);
    }
}
