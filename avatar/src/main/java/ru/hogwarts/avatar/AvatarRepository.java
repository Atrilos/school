package ru.hogwarts.avatar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.shared.avatar.Avatar;
import ru.hogwarts.student.model.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Avatar a WHERE a.id = ?1")
    void removeAvatarById(Long id);

}
