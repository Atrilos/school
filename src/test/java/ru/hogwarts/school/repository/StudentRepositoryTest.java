package ru.hogwarts.school.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:alpine"));

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username=", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password=", postgreSQLContainer::getPassword);
    }

    @Test
    public void countStudentsReturnCorrectResult() {
        Student student1 = new Student(null, "A", 20, null, null);
        entityManager.persist(student1);
        assertThat(studentRepository.countStudents()).isEqualTo(1L);
        Student student2 = new Student(null, "B", 20, null, null);
        entityManager.persist(student2);
        assertThat(studentRepository.countStudents()).isEqualTo(2L);
        Student student3 = new Student(null, "C", 20, null, null);
        entityManager.persist(student3);
        assertThat(studentRepository.countStudents()).isEqualTo(3L);
    }

    @Test
    public void shouldReturnCorrectAverageAge() {
        Student student1 = new Student(null, "A", 20, null, null);
        entityManager.persist(student1);
        assertThat(studentRepository.findAverageAge()).isEqualTo(20.0);
        Student student2 = new Student(null, "B", 30, null, null);
        entityManager.persist(student2);
        assertThat(studentRepository.findAverageAge()).isEqualTo(25.0);
        Student student3 = new Student(null, "C", 30, null, null);
        entityManager.persist(student3);
        assertThat(studentRepository.findAverageAge()).isEqualTo(((double) 20 + 30 + 30) / 3);
    }

    @Test
    public void shouldReturnCorrectLast5Students() {
        Student student1 = new Student(null, "A", 20, null, null);
        Student student2 = new Student(null, "B", 20, null, null);
        Student student3 = new Student(null, "C", 20, null, null);
        Student student4 = new Student(null, "D", 20, null, null);
        Student student5 = new Student(null, "E", 20, null, null);
        Student student6 = new Student(null, "F", 20, null, null);

        List<Student> list = List.of(student1, student2, student3, student4, student5, student6);
        list.forEach(entityManager::persistAndFlush);

        assertThat(studentRepository.findLast5Students())
                .containsExactlyInAnyOrder(student2, student3, student4, student5, student6);
    }
}