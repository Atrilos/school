package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository students;

    public Student addStudent(Student student) {
        return students.save(student);
    }

    public void removeStudent(Long id) {
        students.deleteById(id);
    }

    public Student updateStudent(Student student) {
        return students.save(student);
    }

    public Student getStudentById(Long id) {
        return students.findById(id).orElseThrow(() -> new EntryNotFoundException("The specified student not found"));
    }

    public Collection<Student> getStudentsByAge(int age) {
        return students.findStudentsByAge(age);
    }

    public Collection<Student> getStudentsByAgeBetween(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("Min value can't be more than max value");
        }
        return students.findStudentsByAgeBetween(from, to);
    }

    public Faculty getFacultyByStudent(Long studentId) {
        return students.findStudentsFaculty(studentId);
    }
}
