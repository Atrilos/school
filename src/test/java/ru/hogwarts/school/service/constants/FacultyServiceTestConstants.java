package ru.hogwarts.school.service.constants;

import ru.hogwarts.school.model.Faculty;

import java.util.List;

public class FacultyServiceTestConstants {
    public static final Faculty FACULTY_1 = new Faculty(0L, "1", "1");
    public static final Faculty FACULTY_2 = new Faculty(0L, "2", "2");
    public static final Faculty FACULTY_3 = new Faculty(0L, "3", "3");
    public static final Faculty FACULTY_4 = new Faculty(0L, "4", "4");
    public static final Faculty FACULTY_4_2 = new Faculty(0L, "44", "4");
    public static final Faculty FACULTY_4_3 = new Faculty(0L, "444", "4");
    public static final Faculty FACULTY_5 = new Faculty(0L, "5", "5");
    public static final Faculty FACULTY_5_2 = new Faculty(0L, "55", "5");
    public static final List<Faculty> FACULTY_LIST = List.of(FACULTY_1, FACULTY_2, FACULTY_3,
            FACULTY_4, FACULTY_4_2, FACULTY_4_3, FACULTY_5, FACULTY_5_2);
}
