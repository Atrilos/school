package ru.hogwarts.school.service.constants;

import ru.hogwarts.school.model.Student;

import java.util.List;

public class StudentServiceTestConstants {
    public static final Student STUDENT_1 = new Student(0L, "1", 1);
    public static final Student STUDENT_2 = new Student(0L, "2", 2);
    public static final Student STUDENT_3 = new Student(0L, "3", 3);
    public static final Student STUDENT_4 = new Student(0L, "4", 4);
    public static final Student STUDENT_4_2 = new Student(0L, "44", 4);
    public static final Student STUDENT_4_3 = new Student(0L, "444", 4);
    public static final Student STUDENT_5 = new Student(0L, "5", 5);
    public static final Student STUDENT_5_2 = new Student(0L, "55", 5);
    public static final List<Student> STUDENT_LIST = List.of(STUDENT_1, STUDENT_2, STUDENT_3,
            STUDENT_4, STUDENT_4_2, STUDENT_4_3, STUDENT_5, STUDENT_5_2);
}
