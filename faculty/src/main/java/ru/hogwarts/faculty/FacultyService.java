package ru.hogwarts.faculty;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.shared.exceptions.EntryNotFoundException;
import ru.hogwarts.shared.faculty.FacultyDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper mapper;

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

    protected Faculty getFacultyById(Long id) {
        return facultyRepository
                .findById(id)
                .orElseThrow(() -> new EntryNotFoundException("The specified faculty not found"));
    }

    public FacultyDto getFaculty(Long id) {
        return mapper.toDto(getFacultyById(id));
    }

    public FacultyDto getFacultyByStudent(Long studentId) {
        Faculty faculty = facultyRepository.findFacultiesByStudentId(studentId)
                .orElseThrow(() -> new EntryNotFoundException("The specified faculty not found"));
        return mapper.toDto(faculty);
    }
}
