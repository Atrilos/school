package ru.hogwarts.faculty;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class FacultyNotFound extends RuntimeException {
    public FacultyNotFound() {
        super();
    }

    public FacultyNotFound(String message) {
        super(message);
    }
}
