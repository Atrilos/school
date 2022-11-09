package ru.hogwarts.shared.student;

import lombok.*;
import org.hibernate.Hibernate;
import ru.hogwarts.shared.avatar.Avatar;
import ru.hogwarts.shared.faculty.Faculty;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private Integer age;
    @Column(name = "faculty_id")
    private Long facultyId;
    @Column(name = "avatar_id")
    private Long avatarId;

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
}
