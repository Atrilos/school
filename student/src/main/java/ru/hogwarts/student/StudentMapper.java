package ru.hogwarts.student;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.hogwarts.shared.student.NewStudentDto;
import ru.hogwarts.shared.student.StudentDto;

@Component
public class StudentMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public StudentDto toDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    public Student toEntity(StudentDto studentDto) {
        return modelMapper.map(studentDto, Student.class);
    }

    public Student toEntity(NewStudentDto studentDto) {
        return modelMapper.map(studentDto, Student.class);
    }
}
