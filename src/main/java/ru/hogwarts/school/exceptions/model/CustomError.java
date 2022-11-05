package ru.hogwarts.school.exceptions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({"timestamp", "status", "errorMessage", "message", "path"})
public class CustomError {
    private LocalDateTime timestamp;
    private int status;
    @JsonProperty(value = "error")
    private String errorMessage;
    private String message;
    private String path;

    public CustomError() {
        timestamp = LocalDateTime.now();
    }
}
