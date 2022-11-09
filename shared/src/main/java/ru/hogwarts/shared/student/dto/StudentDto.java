package ru.hogwarts.shared.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hogwarts.shared.avatar.dto.AvatarDto;
import ru.hogwarts.shared.faculty.dto.FacultyDto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.hogwarts.shared.student.Student} entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto implements Serializable {
    private Long id;
    @NotBlank(message = "Name can't be blank")
    private String name;
    private Integer age;
    private FacultyDto faculty;
    private AvatarDto avatar;
}