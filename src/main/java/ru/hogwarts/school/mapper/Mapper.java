package ru.hogwarts.school.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.dto.AvatarDto;
import ru.hogwarts.school.model.dto.FacultyDto;
import ru.hogwarts.school.model.dto.NewStudentDto;
import ru.hogwarts.school.model.dto.StudentDto;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final ModelMapper modelMapper;
    private final AvatarRepository avatarRepository;
    private final FacultyRepository facultyRepository;

    public AvatarDto toDto(Avatar avatar) {
        return modelMapper.map(avatar, AvatarDto.class);
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
