package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.hogwarts.school.service.constants.StudentServiceTestConstants.*;

class StudentServiceTest {

    private StudentService out;

    @BeforeEach
    public void setUp() {
        out = new StudentService();
    }

    @ParameterizedTest
    @MethodSource("provideParamsForDoesntExistTests")
    public void shouldThrow_IfStudentDoesntExist_Remove(Student student, Long id) {
        assertDoesNotThrow(() -> out.addStudent(student));
        assertThrows(EntryNotFoundException.class,
                () -> out.removeStudent(id));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForDoesntExistTests")
    public void shouldThrow_IfStudentDoesntExist_Get(Student student, Long id) {
        assertDoesNotThrow(() -> out.addStudent(student));
        assertThrows(EntryNotFoundException.class,
                () -> out.getStudentById(id));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForDoesntExistUpdateTest")
    public void shouldThrow_IfStudentDoesntExist_Update(Student s1, Student s2) {
        assertDoesNotThrow(() -> out.addStudent(s1));
        assertThrows(EntryNotFoundException.class,
                () -> out.updateStudent(s2));
    }

    @Test
    public void shouldReturnCorrectList_WhenFilterByAge() {
        STUDENT_LIST.forEach(
                s -> assertDoesNotThrow(() -> out.addStudent(s))
        );
        assertIterableEquals(List.of(STUDENT_1),
                out.getStudentsByAge(1));
        assertIterableEquals(Collections.EMPTY_LIST,
                out.getStudentsByAge(10));
        assertIterableEquals(List.of(STUDENT_4, STUDENT_4_2, STUDENT_4_3),
                out.getStudentsByAge(4));
        assertIterableEquals(List.of(STUDENT_5, STUDENT_5_2),
                out.getStudentsByAge(5));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCorrectDeleteTest")
    public void shouldDeleteCorrectEntry(Student s1, Student s2) {
        assertDoesNotThrow(() -> out.addStudent(s1));
        assertDoesNotThrow(() -> out.addStudent(s2));
        assertDoesNotThrow(() -> out.removeStudent(1L));
        assertEquals(s2, out.getStudentById(2L));
        assertThrows(EntryNotFoundException.class, () -> out.getStudentById(1L));
        assertDoesNotThrow(() -> out.addStudent(s1));
        assertDoesNotThrow(() -> out.removeStudent(2L));
        assertEquals(s1, out.getStudentById(3L));
        assertThrows(EntryNotFoundException.class, () -> out.getStudentById(2L));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForDoesntExistUpdateTest")
    public void shouldUpdateCorrectly(Student s1, Student s2) {
        assertDoesNotThrow(() -> out.addStudent(s1));
        s2.setId(1L);
        assertDoesNotThrow(() -> out.updateStudent(s2));
        assertEquals(s2, out.getStudentById(1L));
    }

    @Test
    public void shouldGetCorrectly() {
        STUDENT_LIST.forEach(
                s -> assertDoesNotThrow(() -> out.addStudent(s))
        );
        for (long i = 1; i <= STUDENT_LIST.size(); i++) {
            assertEquals(STUDENT_LIST.get((int) (i - 1)),
                    out.getStudentById(i));
        }
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

    public static Stream<Arguments> provideParamsForDoesntExistUpdateTest() {
        return Stream.of(
                Arguments.of(
                        new Student(0L, "example", 10),
                        new Student(0L, "changed", 11)
                ),
                Arguments.of(
                        new Student(0L, "example", 10),
                        new Student(2L, "changed", 11)
                ));
    }

    public static Stream<Arguments> provideParamsForCorrectDeleteTest() {
        return Stream.of(
                Arguments.of(STUDENT_1, STUDENT_2),
                Arguments.of(STUDENT_2, STUDENT_3),
                Arguments.of(STUDENT_4, STUDENT_4_2)
        );
    }
}