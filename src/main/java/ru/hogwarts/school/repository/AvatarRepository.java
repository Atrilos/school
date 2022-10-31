package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    @Query("SELECT a FROM Avatar a WHERE a.student.id = ?1")
    Optional<Avatar> findByStudentId(Long studentId);
}
