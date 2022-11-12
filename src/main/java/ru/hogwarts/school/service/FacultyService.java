package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.mapper.Mapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.dto.FacultyDto;
import ru.hogwarts.school.model.dto.StudentDto;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final Mapper mapper;

    public FacultyDto addFaculty(FacultyDto faculty) {
        log.info("Add new faculty with name={}, color={}",
                faculty.getName(), faculty.getColor());
        faculty.setId(null);
        Faculty facultyToAdd = mapper.toEntity(faculty);
        return mapper.toDto(facultyRepository.save(facultyToAdd));
    }

    public void removeFaculty(Long id) {
        log.info("Delete faculty with id={}", id);
        getFacultyById(id);
        facultyRepository.deleteById(id);
    }

    public FacultyDto updateFaculty(long id, FacultyDto faculty) {
        log.info("Update faculty with id={}", id);
        Faculty foundFaculty = getFacultyById(id);
        foundFaculty.setName(faculty.getName());
        foundFaculty.setColor(faculty.getColor());
        return mapper.toDto(facultyRepository.save(foundFaculty));
    }

    public Collection<FacultyDto> getFacultiesByColor(String color) {
        log.info("Get all faculties with color={}", color);
        return facultyRepository.
                findFacultiesByColor(color)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public Collection<FacultyDto> getFacultiesByName(String name) {
        log.info("Get all faculties with name={}", name);
        return facultyRepository
                .findFacultiesByName(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public Collection<StudentDto> getFacultyStudents(String facultyName) {
        log.info("Get all students from facultyName={}", facultyName);
        return facultyRepository
                .findByFacultyName(facultyName)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public FacultyDto getFaculty(Long id) {
        log.info("Get faculty with id={}", id);
        return mapper.toDto(getFacultyById(id));
    }

    protected Faculty getFacultyById(Long id) {
        return facultyRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Faculty with id={} doesn't exist", id);
                    return new EntryNotFoundException("The specified faculty not found");
                });
    }
}
