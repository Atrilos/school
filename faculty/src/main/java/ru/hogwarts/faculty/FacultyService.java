package ru.hogwarts.faculty;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.shared.faculty.Faculty;
import ru.hogwarts.shared.faculty.dto.FacultyDto;
import ru.hogwarts.shared.mapper.Mapper;
import ru.hogwarts.shared.student.dto.StudentDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final Mapper mapper;

    public FacultyDto addFaculty(FacultyDto faculty) {
        faculty.setId(null);
        Faculty facultyToAdd = mapper.toEntity(faculty);
        return mapper.toDto(facultyRepository.save(facultyToAdd));
    }

    public void removeFaculty(Long id) {
        getFacultyById(id);
        facultyRepository.deleteById(id);
    }

    public FacultyDto updateFaculty(long id, FacultyDto faculty) {
        Faculty foundFaculty = getFacultyById(id);
        foundFaculty.setName(faculty.getName());
        foundFaculty.setColor(faculty.getColor());
        return mapper.toDto(facultyRepository.save(foundFaculty));
    }

    public Collection<FacultyDto> getFacultiesByColor(String color) {
        return facultyRepository.
                findFacultiesByColor(color)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public Collection<FacultyDto> getFacultiesByName(String name) {
        return facultyRepository
                .findFacultiesByName(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public Collection<StudentDto> getFacultyStudents(String facultyName) {
        return facultyRepository
                .findByFacultyName(facultyName)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository
                .findById(id)
                .orElseThrow(() -> new FacultyNotFound("The specified faculty not found"));
    }
}
