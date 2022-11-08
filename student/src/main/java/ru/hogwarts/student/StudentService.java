package ru.hogwarts.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.student.exceptions.EntryNotFoundException;
import ru.hogwarts.student.mapper.Mapper;
import ru.hogwarts.student.model.Avatar;
import ru.hogwarts.student.model.Faculty;
import ru.hogwarts.student.model.Student;
import ru.hogwarts.student.model.dto.FacultyDto;
import ru.hogwarts.student.model.dto.NewStudentDto;
import ru.hogwarts.student.model.dto.StudentDto;
import ru.hogwarts.student.repository.StudentRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
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

    public StudentDto updateStudent(long id, StudentDto student) {
        Student foundStudent = getStudentById(id);

        Faculty faculty = null;
        if (student.getFaculty() != null)
            faculty = facultyService.getFacultyById(student.getFaculty().getId());

        Avatar avatar = null;
        if (student.getAvatar() != null)
            avatar = avatarService.getAvatarById(student.getAvatar().getId());

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
        Faculty faculty = studentRepository.findStudentsFaculty(studentId);
        return mapper.toDto(faculty);
    }

    public StudentDto getStudent(long id) {
        Student foundStudent = getStudentById(id);
        return mapper.toDto(foundStudent);
    }

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
