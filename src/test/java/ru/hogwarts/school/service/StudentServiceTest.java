package ru.hogwarts.school.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;
    @InjectMocks
    private StudentService out;

    @ParameterizedTest
    @MethodSource("provideParamsForDoesntExistTests")
    public void shouldThrow_IfStudentDoesntExist_Remove(Student student, Long id) {
        when(repository.save(any(Student.class))).thenAnswer(i -> {
            Student tmp = i.getArgument(0, Student.class);
            tmp.setId(1L);
            return tmp;
        });
        when(repository.findById(longThat(l -> l != 1L))).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> out.addStudent(student));
        assertThrows(EntryNotFoundException.class,
                () -> out.getStudentById(id));
    }

    public static Stream<Arguments> provideParamsForDoesntExistTests() {
        return Stream.of(
                Arguments.of(new Student(0L, "example", 10), 0L),
                Arguments.of(new Student(0L, "example", 10), 2L),
                Arguments.of(new Student(0L, "example", 10), 3L),
                Arguments.of(new Student(0L, "example", 10), -1L),
                Arguments.of(new Student(0L, "example", 10), -5L)
        );
    }

}