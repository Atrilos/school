package ru.hogwarts.school.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.dto.NewStudentDto;
import ru.hogwarts.school.model.dto.StudentDto;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;

@Mapper(componentModel = "spring",
        uses = {AvatarMapper.class, FacultyMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class StudentMapper {

    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private FacultyRepository facultyRepository;

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

    public abstract StudentDto toDto(Student student);

    public abstract Student toEntity(StudentDto studentDto);
}
