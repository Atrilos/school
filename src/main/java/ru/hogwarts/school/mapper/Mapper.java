package ru.hogwarts.student.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.hogwarts.student.exceptions.EntryNotFoundException;
import ru.hogwarts.student.model.Avatar;
import ru.hogwarts.student.model.Faculty;
import ru.hogwarts.student.model.Student;
import ru.hogwarts.student.model.dto.AvatarDto;
import ru.hogwarts.student.model.dto.FacultyDto;
import ru.hogwarts.student.model.dto.NewStudentDto;
import ru.hogwarts.student.model.dto.StudentDto;
import ru.hogwarts.student.repository.AvatarRepository;
import ru.hogwarts.student.repository.FacultyRepository;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final ModelMapper modelMapper;
    private final AvatarRepository avatarRepository;
    private final FacultyRepository facultyRepository;
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
            avatar = avatarRepository
                    .findById(studentDto.getAvatarId())
                    .orElseThrow(() -> new EntryNotFoundException("The specified avatar not found"));
        }
        Faculty faculty = null;
        if (studentDto.getFacultyId() != null && studentDto.getFacultyId() > 0) {
            faculty = facultyRepository
                    .findById(studentDto.getFacultyId())
                    .orElseThrow(() -> new EntryNotFoundException("The specified faculty not found"));
        }
        return student
                .faculty(faculty)
                .avatar(avatar)
                .build();
    }
}
