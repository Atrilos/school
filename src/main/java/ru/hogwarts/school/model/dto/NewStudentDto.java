package ru.hogwarts.student.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewStudentDto {
    @NotBlank(message = "Name can't be blank")
    private String name;
    private int age;
    private Long facultyId;
    private Long avatarId;
}
