package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.mapper.Mapper;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.dto.FacultyDto;
import ru.hogwarts.school.model.dto.NewStudentDto;
import ru.hogwarts.school.model.dto.StudentDto;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final FacultyService facultyService;
    private final AvatarService avatarService;
    private final StudentRepository studentRepository;
    private final Mapper mapper;

    public StudentDto addStudent(NewStudentDto student) {
        Student studentToAdd = mapper.toEntity(student);
        return mapper.toDto(studentRepository.save(studentToAdd));
    }

    public void removeStudent(Long id) {
        getStudentById(id);
        studentRepository.deleteById(id);
    }

    @Transactional
    public StudentDto updateStudent(long id, NewStudentDto student) {
        Student foundStudent = getStudentById(id);

        Faculty faculty = null;
        if (student.getFacultyId() != null)
            faculty = facultyService.getFacultyById(student.getFacultyId());

        Avatar avatar = null;
        if (student.getAvatarId() != null)
            avatar = avatarService.getAvatarById(student.getAvatarId());

        foundStudent.setName(student.getName());
        foundStudent.setFaculty(faculty);
        foundStudent.setAvatar(avatar);
        return mapper.toDto(studentRepository.save(foundStudent));
    }

    public Collection<StudentDto> getStudentsByAge(int age) {
        return studentRepository
                .findStudentsByAge(age)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public Collection<StudentDto> getStudentsByAgeBetween(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("Min value can't be more than max value");
        }
        return studentRepository
                .findStudentsByAgeBetween(from, to)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public FacultyDto getFacultyByStudent(Long studentId) {
        Faculty faculty = studentRepository
                .findStudentsFaculty(studentId)
                .orElseThrow(() -> new EntryNotFoundException("The specified student not found"));
        return mapper.toDto(faculty);
    }

    public StudentDto getStudent(long id) {
        Student foundStudent = getStudentById(id);
        return mapper.toDto(foundStudent);
    }

    @Transactional
    public StudentDto patchStudentAvatar(long id, long avatarId) {
        Student student = getStudentById(id);
        Avatar avatar = avatarService.getAvatarById(avatarId);
        student.setAvatar(avatar);
        return mapper.toDto(studentRepository.save(student));
    }

    protected Student getStudentById(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new EntryNotFoundException("The specified student not found"));
    }
}
