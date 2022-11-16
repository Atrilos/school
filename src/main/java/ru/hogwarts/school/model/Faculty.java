package ru.hogwarts.school.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Slf4j
@Table(name = "faculty")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "color")
    private String color;
    @OneToMany(mappedBy = "faculty")
    private Set<Student> students = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Faculty faculty = (Faculty) o;
        return id != null && Objects.equals(id, faculty.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @PostRemove
    public void logFacultyRemoval() {
        log.info("Faculty with id={} removed", id);
    }

    @PostPersist
    public void logFacultyAdded() {
        log.info("Add new faculty with name={}, color={}", name, color);
    }

    @PostUpdate
    public void logFacultyUpdated() {
        log.info("Faculty with id={} updated", id);
    }
}
