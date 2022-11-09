package ru.hogwarts.shared.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.hogwarts.shared.avatar.Avatar;
import ru.hogwarts.shared.avatar.AvatarClient;
import ru.hogwarts.shared.avatar.dto.AvatarDto;
import ru.hogwarts.shared.faculty.Faculty;
import ru.hogwarts.shared.faculty.FacultyClient;
import ru.hogwarts.shared.faculty.dto.FacultyDto;
import ru.hogwarts.shared.student.Student;
import ru.hogwarts.shared.student.dto.NewStudentDto;
import ru.hogwarts.shared.student.dto.StudentDto;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final ModelMapper modelMapper;
    private final AvatarClient avatarClient;
    private final FacultyClient facultyClient;
    @Value("${server.port}")
    private String port;

    public AvatarDto toDto(Avatar avatar) {
        AvatarDto avatarDto = modelMapper.map(avatar, AvatarDto.class);
        avatarDto.setUrl("http://localhost:%s/avatar/%d/from-db".formatted(port, avatar.getId()));
        return avatarDto;
    }

    public FacultyDto toDto(Faculty faculty) {
        return modelMapper.map(faculty, FacultyDto.class);
    }

    public Faculty toEntity(FacultyDto facultyDto) {
        return modelMapper.map(facultyDto, Faculty.class);
    }

    public StudentDto toDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    public Student toEntity(StudentDto studentDto) {
        return modelMapper.map(studentDto, Student.class);
    }

    public Student toEntity(NewStudentDto studentDto) {
        Student.StudentBuilder student = Student.builder()
                .age(studentDto.getAge())
                .name(studentDto.getName());
        Avatar avatar = null;
        if (studentDto.getAvatarId() != null && studentDto.getAvatarId() > 0) {
            avatar = avatarClient.getAvatarById(studentDto.getAvatarId()).getBody();
        }
        Faculty faculty = null;
        if (studentDto.getFacultyId() != null && studentDto.getFacultyId() > 0) {
            faculty = facultyClient.getFaculty(studentDto.getFacultyId()).getBody();
        }
        return student
                .faculty(faculty)
                .avatar(avatar)
                .build();
    }
}
