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
                .orElseThrow(() -> new EntryNotFoundException("Faculty with id=" + id + " doesn't exist",
                        "The specified faculty not found"));
    }

    public String getLongestName() {
        log.info("Get the longest faculty name");
        return facultyRepository
                .findLongestName()
                .orElseThrow(() -> new EntryNotFoundException("No faculties present in the Faculty table or names undefined",
                        "No faculties present in the Faculty table or names undefined"));
    }
}
