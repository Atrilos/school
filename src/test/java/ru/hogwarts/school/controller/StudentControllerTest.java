package ru.hogwarts.school.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static ru.hogwarts.school.controller.constants.StudentControllerTestConstants.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class StudentControllerTest {

    private static final Gson GSON = new Gson();
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:alpine"));
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username=", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password=", postgreSQLContainer::getPassword);
    }

    public static Stream<Arguments> provideParamsForStudentsFoundByAgeBetweenCorrectTest() {
        return Stream.of(
                Arguments.of(20, 21, 2),
                Arguments.of(19, 20, 2),
                Arguments.of(0, 20, 2),
                Arguments.of(0, 25, 3)
        );
    }

    public static Stream<Arguments> provideParamsForStudentsFoundByAgeBetweenOutOfBoundsTest() {
        return Stream.of(
                Arguments.of(15, 19),
                Arguments.of(19, 19),
                Arguments.of(25, 30)
        );
    }

    @AfterEach
    public void deleteEntities() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    private Student deepCopyAndChangeId(Student student, long id) {
        return Student.builder()
                .id(id)
                .age(student.getAge())
                .name(student.getName())
                .faculty(student.getFaculty())
                .avatar(student.getAvatar())
                .build();
    }

    @Test
    public void shouldReturnCorrectStudent_FoundById() {
        Long id = studentRepository.save(BASIC_STUDENT).getId();
        Student actual = webTestClient.get()
                .uri("/student/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Student.class)
                .returnResult().getResponseBody();
        Student expected = deepCopyAndChangeId(BASIC_STUDENT, id);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAddStudentCorrectly() {
        Student actual = webTestClient.post()
                .uri("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(GSON.toJson(BASIC_STUDENT))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Student.class)
                .returnResult().getResponseBody();
        assertThat(actual.getId()).isNotNull().isGreaterThan(0L);
        long id = actual.getId();
        Student expected = deepCopyAndChangeId(BASIC_STUDENT, id);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUpdateCorrectStudent() {
        Long id = studentRepository.save(BASIC_STUDENT).getId();
        Student expected = deepCopyAndChangeId(BASIC_STUDENT_2, id);
        Student actual = webTestClient.put()
                .uri("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(GSON.toJson(expected))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Student.class)
                .returnResult().getResponseBody();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldDeleteCorrectStudent() {
        Long firstId = studentRepository.save(BASIC_STUDENT).getId();
        Long secondId = studentRepository.save(BASIC_STUDENT_2).getId();
        Student firstStudent = deepCopyAndChangeId(BASIC_STUDENT, firstId);
        Student secondStudent = deepCopyAndChangeId(BASIC_STUDENT_2, secondId);
        webTestClient.delete()
                .uri("/student/{id}", firstId)
                .exchange()
                .expectStatus().isOk();
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).contains(secondStudent).doesNotContain(firstStudent);
    }

    @Test
    public void shouldReturnCorrectStudent_FoundByAge() {
        Long firstId = studentRepository.save(BASIC_STUDENT).getId();
        Long secondId = studentRepository.save(BASIC_STUDENT_3).getId();
        Student expected = deepCopyAndChangeId(BASIC_STUDENT, firstId);
        Student notExpected = deepCopyAndChangeId(BASIC_STUDENT_3, secondId);
        final int age = 20;
        List<?> list = (List<?>) webTestClient.get()
                .uri("/student?age={age}", age)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .returnResult().getResponseBody();
        List<Student> resultList = list.stream().map(o -> GSON.fromJson(GSON.toJsonTree(o), Student.class)).toList();
        assertThat(resultList).contains(expected).doesNotContain(notExpected);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForStudentsFoundByAgeBetweenCorrectTest")
    public void shouldReturnCorrectStudents_FoundByAgeBetween(int from, int to, int expectedSize) {
        studentRepository.save(BASIC_STUDENT);
        studentRepository.save(BASIC_STUDENT_2);
        studentRepository.save(BASIC_STUDENT_3);
        List<?> list = (List<?>) webTestClient.get()
                .uri("/student?from={from}&to={to}", from, to)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .returnResult().getResponseBody();
        assertThat(list.size()).isEqualTo(expectedSize);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForStudentsFoundByAgeBetweenOutOfBoundsTest")
    public void shouldReturnEmptyList_FoundByAgeBetweenNotInbounds(int from, int to) {
        studentRepository.save(BASIC_STUDENT);
        studentRepository.save(BASIC_STUDENT_2);
        studentRepository.save(BASIC_STUDENT_3);
        List<?> list = (List<?>) webTestClient.get()
                .uri("/student?from={from}&to={to}", from, to)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .returnResult().getResponseBody();
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnCorrectFaculty() {
        Faculty expectedFaculty = new Faculty(0L, "S", "Black", null);
        expectedFaculty = facultyRepository.save(expectedFaculty);
        Student expectedStudent = deepCopyAndChangeId(BASIC_STUDENT, 0L);
        expectedStudent.setFaculty(expectedFaculty);
        Long id = studentRepository.save(expectedStudent).getId();
        Faculty actualFaculty = webTestClient.get()
                .uri("/student?studentId={id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Faculty.class)
                .returnResult().getResponseBody();
        assertThat(expectedFaculty).isEqualTo(actualFaculty);
    }
}