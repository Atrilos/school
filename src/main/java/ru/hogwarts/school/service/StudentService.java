package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
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
public class StudentService {

    private final FacultyService facultyService;
    private final AvatarService avatarService;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;

    public StudentDto addStudent(NewStudentDto student) {
        Student studentToAdd = studentMapper.toEntity(student);
        return studentMapper.toDto(studentRepository.save(studentToAdd));
    }

    public void removeStudent(Long id) {
        getStudentById(id);
        studentRepository.deleteById(id);
    }

    public StudentDto updateStudent(long id, StudentDto student) {
        Student foundStudent = getStudentById(id);
        Faculty faculty = facultyService.getFacultyById(student.getFaculty().getId());
        Avatar avatar = avatarService.getAvatarById(student.getAvatar().getId());
        foundStudent.setName(student.getName());
        foundStudent.setFaculty(faculty);
        foundStudent.setAvatar(avatar);
        return studentMapper.toDto(studentRepository.save(foundStudent));
    }

    public Collection<StudentDto> getStudentsByAge(int age) {
        return studentRepository
                .findStudentsByAge(age)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public Collection<StudentDto> getStudentsByAgeBetween(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("Min value can't be more than max value");
        }
        return studentRepository
                .findStudentsByAgeBetween(from, to)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public FacultyDto getFacultyByStudent(Long studentId) {
        Faculty faculty = studentRepository.findStudentsFaculty(studentId);
        return facultyMapper.toDto(faculty);
    }

    public StudentDto getStudent(long id) {
        Student foundStudent = getStudentById(id);
        return studentMapper.toDto(foundStudent);
    }

    protected Student getStudentById(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new EntryNotFoundException("The specified student not found"));
    }

    public StudentDto patchStudentAvatar(long id, long avatarId) {
        Student student = getStudentById(id);
        Avatar avatar = avatarService.getAvatarById(avatarId);
        student.setAvatar(avatar);
        return studentMapper.toDto(studentRepository.save(student));
    }
}
