package ru.hogwarts.faculty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Query("SELECT f FROM Faculty f WHERE f.color = ?1")
    List<Faculty> findFacultiesByColor(String color);

    @Query(value = "SELECT f FROM Faculty f WHERE LOWER(f.name) LIKE LOWER(?1)")
    Collection<Faculty> findFacultiesByName(String name);

    @Query(value = """
            SELECT f FROM faculty f JOIN student s ON f.id = s.faculty_id WHERE s.id = ?
            """, nativeQuery = true)
    Optional<Faculty> findFacultiesByStudentId(Long studentId);

}
