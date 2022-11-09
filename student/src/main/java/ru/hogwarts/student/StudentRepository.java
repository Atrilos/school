package ru.hogwarts.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.age = ?1")
    List<Student> findStudentsByAge(int age);

    @Query(value = "SELECT * FROM student WHERE age BETWEEN ?1 AND ?2",
            nativeQuery = true)
    Collection<Student> findStudentsByAgeBetween(int from, int to);

    @Query(value = """
            SELECT count(*) FROM student
            """, nativeQuery = true)
    long countStudents();

    @Query(value = """
            SELECT AVG(age) FROM student
            """, nativeQuery = true)
    double findAverageAge();

    @Query(value = """
            SELECT * FROM student
            ORDER BY id DESC
            LIMIT 5
            """, nativeQuery = true)
    List<Student> findLast5Students();

    @Query(value = """
            SELECT s from Student s
            JOIN faculty f ON s.faculty_id = f.id
            WHERE f.name = ?1
            """, nativeQuery = true)
    Collection<Student> findStudentsByFacultyName(String facultyName);
}
