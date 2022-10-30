package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository faculties;

    public Faculty addFaculty(Faculty faculty) {
        return faculties.save(faculty);
    }

    public void removeFaculty(Long id) {
        faculties.deleteById(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        return faculties.save(faculty);
    }

    public Faculty getFacultyById(Long id) {
        Optional<Faculty> foundFaculty = faculties.findById(id);
        if (foundFaculty.isEmpty()) {
            throw new EntryNotFoundException("The specified faculty not found");
        }
        return foundFaculty.get();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        return faculties.findFacultiesByColor(color);
    }
}
