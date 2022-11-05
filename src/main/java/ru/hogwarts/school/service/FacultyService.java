package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.dto.FacultyDto;
import ru.hogwarts.school.model.dto.StudentDto;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository faculties;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;

    public FacultyDto addFaculty(FacultyDto faculty) {
        faculty.setId(null);
        Faculty facultyToAdd = facultyMapper.toEntity(faculty);
        return facultyMapper.toDto(faculties.save(facultyToAdd));
    }

    public void removeFaculty(Long id) {
        getFacultyById(id);
        faculties.deleteById(id);
    }

    public FacultyDto updateFaculty(long id, FacultyDto faculty) {
        Faculty foundFaculty = getFacultyById(id);
        foundFaculty.setName(faculty.getName());
        foundFaculty.setColor(faculty.getColor());
        return facultyMapper.toDto(faculties.save(foundFaculty));
    }

    public Collection<FacultyDto> getFacultiesByColor(String color) {
        return faculties.
                findFacultiesByColor(color)
                .stream()
                .map(facultyMapper::toDto)
                .toList();
    }

    public Collection<FacultyDto> getFacultiesByName(String name) {
        return faculties
                .findFacultiesByName(name)
                .stream()
                .map(facultyMapper::toDto)
                .toList();
    }

    public Collection<StudentDto> getFacultyStudents(String facultyName) {
        return faculties
                .findByFacultyName(facultyName)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public FacultyDto getFaculty(Long id) {
        return facultyMapper.toDto(getFacultyById(id));
    }

    protected Faculty getFacultyById(Long id) {
        return faculties
                .findById(id)
                .orElseThrow(() -> new EntryNotFoundException("The specified faculty not found"));
    }
}
