package ru.hogwarts.school.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.dto.FacultyDto;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FacultyMapper {
    FacultyDto toDto(Faculty faculty);

    Faculty toEntity(FacultyDto facultyDto);
}
