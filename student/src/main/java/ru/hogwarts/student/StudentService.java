package ru.hogwarts.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.shared.avatar.Avatar;
import ru.hogwarts.shared.avatar.AvatarClient;
import ru.hogwarts.shared.exceptions.EntryNotFoundException;
import ru.hogwarts.shared.faculty.Faculty;
import ru.hogwarts.shared.faculty.FacultyClient;
import ru.hogwarts.shared.faculty.dto.FacultyDto;
import ru.hogwarts.shared.mapper.Mapper;
import ru.hogwarts.shared.student.Student;
import ru.hogwarts.shared.student.dto.NewStudentDto;
import ru.hogwarts.shared.student.dto.StudentDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyClient facultyClient;
    private final AvatarClient avatarClient;
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
            faculty = facultyClient
                    .getFaculty(student.getFaculty().getId())
                    .getBody();

        Avatar avatar = null;
        if (student.getAvatar() != null)
            avatar = avatarClient
                    .getAvatarById(student.getAvatar().getId())
                    .getBody();

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
        Avatar avatar = avatarClient
                .getAvatarById(avatarId)
                .getBody();
        student.setAvatar(avatar);
        return mapper.toDto(studentRepository.save(student));
    }

    protected Student getStudentById(Long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new EntryNotFoundException("The specified student not found"));
    }

    public Collection<StudentDto> getFacultyStudents(String facultyName) {
        return studentRepository.
                findStudentsByFacultyName(facultyName)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
