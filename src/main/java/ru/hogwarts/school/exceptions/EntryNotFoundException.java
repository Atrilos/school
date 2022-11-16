package ru.hogwarts.school.exceptions;

public class EntryNotFoundException extends RuntimeException {

    private final String logMessage;

    public EntryNotFoundException(String logMessage, String message) {
        super(message);
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
