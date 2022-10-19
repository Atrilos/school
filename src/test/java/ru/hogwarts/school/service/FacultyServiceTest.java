package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.hogwarts.school.service.constants.FacultyServiceTestConstants.*;

class FacultyServiceTest {

    private FacultyService out;

    @BeforeEach
    public void setUp() {
        out = new FacultyService();
    }

    @ParameterizedTest
    @MethodSource("provideParamsForDoesntExistTests")
    public void shouldThrow_IfStudentDoesntExist_Remove(Faculty faculty, Long id) {
        assertDoesNotThrow(() -> out.addFaculty(faculty));
        assertThrows(EntryNotFoundException.class,
                () -> out.removeFaculty(id));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForDoesntExistTests")
    public void shouldThrow_IfStudentDoesntExist_Get(Faculty faculty, Long id) {
        assertDoesNotThrow(() -> out.addFaculty(faculty));
        assertThrows(EntryNotFoundException.class,
                () -> out.getFacultyById(id));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForDoesntExistUpdateTest")
    public void shouldThrow_IfStudentDoesntExist_Update(Faculty f1, Faculty f2) {
        assertDoesNotThrow(() -> out.addFaculty(f1));
        assertThrows(EntryNotFoundException.class,
                () -> out.updateFaculty(f2));
    }

    @Test
    public void shouldReturnCorrectList_WhenFilterByAge() {
        FACULTY_LIST.forEach(
                s -> assertDoesNotThrow(() -> out.addFaculty(s))
        );
        assertIterableEquals(List.of(FACULTY_1),
                out.getFacultiesByColor("1"));
        assertIterableEquals(Collections.EMPTY_LIST,
                out.getFacultiesByColor("10"));
        assertIterableEquals(List.of(FACULTY_4, FACULTY_4_2, FACULTY_4_3),
                out.getFacultiesByColor("4"));
        assertIterableEquals(List.of(FACULTY_5, FACULTY_5_2),
                out.getFacultiesByColor("5"));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCorrectDeleteTest")
    public void shouldDeleteCorrectEntry(Faculty f1, Faculty f2) {
        assertDoesNotThrow(() -> out.addFaculty(f1));
        assertDoesNotThrow(() -> out.addFaculty(f2));
        assertDoesNotThrow(() -> out.removeFaculty(1L));
        assertEquals(f2, out.getFacultyById(2L));
        assertThrows(EntryNotFoundException.class, () -> out.getFacultyById(1L));
        assertDoesNotThrow(() -> out.addFaculty(f1));
        assertDoesNotThrow(() -> out.removeFaculty(2L));
        assertEquals(f1, out.getFacultyById(3L));
        assertThrows(EntryNotFoundException.class, () -> out.getFacultyById(2L));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForDoesntExistUpdateTest")
    public void shouldUpdateCorrectly(Faculty f1, Faculty f2) {
        assertDoesNotThrow(() -> out.addFaculty(f1));
        f2.setId(1L);
        assertDoesNotThrow(() -> out.updateFaculty(f2));
        assertEquals(f2, out.getFacultyById(1L));
    }

    @Test
    public void shouldGetCorrectly() {
        FACULTY_LIST.forEach(
                s -> assertDoesNotThrow(() -> out.addFaculty(s))
        );
        for (long i = 1; i <= FACULTY_LIST.size(); i++) {
            assertEquals(FACULTY_LIST.get((int) (i - 1)),
                    out.getFacultyById(i));
        }
    }

    public static Stream<Arguments> provideParamsForDoesntExistTests() {
        return Stream.of(
                Arguments.of(new Faculty(0L, "example", "10"), 0L),
                Arguments.of(new Faculty(0L, "example", "10"), 2L),
                Arguments.of(new Faculty(0L, "example", "10"), 3L),
                Arguments.of(new Faculty(0L, "example", "10"), -1L),
                Arguments.of(new Faculty(0L, "example", "10"), -5L)
        );
    }

    public static Stream<Arguments> provideParamsForDoesntExistUpdateTest() {
        return Stream.of(
                Arguments.of(
                        new Faculty(0L, "example", "10"),
                        new Faculty(0L, "changed", "11")
                ),
                Arguments.of(
                        new Faculty(0L, "example", "10"),
                        new Faculty(2L, "changed", "11")
                ));
    }

    public static Stream<Arguments> provideParamsForCorrectDeleteTest() {
        return Stream.of(
                Arguments.of(FACULTY_1, FACULTY_2),
                Arguments.of(FACULTY_2, FACULTY_3),
                Arguments.of(FACULTY_4, FACULTY_4_2)
        );
    }
}