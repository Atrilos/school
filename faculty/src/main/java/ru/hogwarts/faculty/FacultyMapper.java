package ru.hogwarts.faculty;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.hogwarts.shared.faculty.FacultyDto;

@Component
public class FacultyMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public FacultyDto toDto(Faculty faculty) {
        return modelMapper.map(faculty, FacultyDto.class);
    }

    public Faculty toEntity(FacultyDto facultyDto) {
        return modelMapper.map(facultyDto, Faculty.class);
    }
}
