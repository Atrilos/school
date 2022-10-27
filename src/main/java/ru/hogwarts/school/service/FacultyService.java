package ru.hogwarts.school.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Data
public class FacultyService {

    private final Map<Long, Faculty> faculties;
    private Long idCounter;

    public FacultyService() {
        faculties = new HashMap<>();
        idCounter = 0L;
    }

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++idCounter);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty removeFaculty(Long id) {
        Optional<Faculty> foundFaculty = Optional.ofNullable(faculties.remove(id));
        if (foundFaculty.isEmpty()) {
            throw new EntryNotFoundException("The specified faculty not found");
        }
        return foundFaculty.get();
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            throw new EntryNotFoundException("The specified faculty not found");
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFacultyById(Long id) {
        Optional<Faculty> foundFaculty = Optional.ofNullable(faculties.get(id));
        if (foundFaculty.isEmpty()) {
            throw new EntryNotFoundException("The specified faculty not found");
        }
        return foundFaculty.get();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        return faculties.values().stream()
                .filter(f -> f.getColor().equalsIgnoreCase(color))
                .toList();
    }
}
