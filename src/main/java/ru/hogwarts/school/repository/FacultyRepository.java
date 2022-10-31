package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Query("SELECT f FROM Faculty f WHERE f.color = ?1")
    List<Faculty> findFacultiesByColor(String color);

    @Query(value = "SELECT f FROM Faculty f WHERE LOWER(f.name) LIKE LOWER(?1)")
    Collection<Faculty> findFacultiesByName(String name);

    @Query(value = """
            SELECT f.students FROM Faculty f
            WHERE f.name = ?1
            """
    )
    Collection<Student> findByFacultyName(String facultyName);
}