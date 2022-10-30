package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

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
        Optional<Student> foundStudent = students.findById(id);
        if (foundStudent.isEmpty()) {
            throw new EntryNotFoundException("The specified student not found");
        }
        return foundStudent.get();
    }

    public Collection<Student> getStudentsByAge(int age) {
        return students.findStudentsByAge(age);
    }
}
