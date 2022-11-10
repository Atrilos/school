package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.age = ?1")
    List<Student> findStudentsByAge(int age);

    @Query(value = "SELECT * FROM student WHERE age BETWEEN ?1 AND ?2",
            nativeQuery = true)
    Collection<Student> findStudentsByAgeBetween(int from, int to);

    @Query("""
            SELECT s.faculty
            FROM Student s
            WHERE s.id = ?1
            """)
    Faculty findStudentsFaculty(Long id);

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
}
