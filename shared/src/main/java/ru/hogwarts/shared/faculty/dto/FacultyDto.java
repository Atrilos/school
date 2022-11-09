package ru.hogwarts.shared.faculty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.hogwarts.shared.faculty.Faculty} entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDto implements Serializable {
    private Long id;
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotBlank(message = "Color can't be blank")
    private String color;
}
