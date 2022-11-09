package ru.hogwarts.shared.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewStudentDto {
    @NotBlank(message = "Name can't be blank")
    private String name;
    private int age;
    @Min(1L)
    private Long facultyId;
    @Min(1L)
    private Long avatarId;
}
