package ru.hogwarts.school.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Data
public class StudentService {
    private final Map<Long, Student> students;
    private Long idCounter;

    public StudentService() {
        students = new HashMap<>();
        idCounter = 0L;
    }

    public Student addStudent(Student student) {
        student.setId(++idCounter);
        students.put(student.getId(), student);
        return student;
    }

    public Student removeStudent(Long id) {
        Optional<Student> foundStudent = Optional.ofNullable(students.remove(id));
        if (foundStudent.isEmpty()) {
            throw new EntryNotFoundException("The specified student not found");
        }
        return foundStudent.get();
    }

    public Student updateStudent(Student student) {
        if (!students.containsKey(student.getId())) {
            throw new EntryNotFoundException("The specified faculty not found");
        }
        students.put(student.getId(), student);
        return student;
    }

    public Student getStudentById(Long id) {
        Optional<Student> foundStudent = Optional.ofNullable(students.get(id));
        if (foundStudent.isEmpty()) {
            throw new EntryNotFoundException("The specified student not found");
        }
        return foundStudent.get();
    }

    public Collection<Student> getStudentsByAge(int age) {
        return students.values()
                .stream()
                .filter(s -> s.getAge() == age)
                .toList();
    }
}
