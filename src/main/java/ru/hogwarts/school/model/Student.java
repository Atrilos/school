package ru.hogwarts.school.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Slf4j
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
    @OneToOne(mappedBy = "student")
    private Avatar avatar;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Student student = (Student) o;
        return id != null && Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @PostRemove
    public void logStudentRemoval() {
        log.info("Student with id={} removed", id);
    }

    @PostPersist
    public void logStudentAdded() {
        log.info(
                "Added student: name={}, age={}, facultyId={}, avatarId={}",
                name,
                age,
                faculty != null ? faculty.getId() : "null",
                id
        );
    }
}
