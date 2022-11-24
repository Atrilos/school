package ru.hogwarts.school.repository;

import com.github.javafaker.Faker;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StudentRepositoryFilling {

    private final Faker faker = new Faker();
    @Autowired
    private StudentRepository studentRepository;

    @Test
    @Ignore
    public void dbFiller() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Student student = Student.builder()
                    .id(null)
                    .avatar(null)
                    .faculty(null)
                    .age(faker.random().nextInt(20, 40))
                    .name(faker.harryPotter().character())
                    .build();
            students.add(student);
        }
        studentRepository.saveAll(students);
    }
}