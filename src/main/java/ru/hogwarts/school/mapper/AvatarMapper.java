package ru.hogwarts.school.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.dto.AvatarDto;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AvatarMapper {
    @Mapping(target = "url",
            expression = "java(\"http://localhost:8080/avatar/\" + avatar.getId() + \"/from-db\")")
    AvatarDto toDto(Avatar avatar);

    Avatar toEntity(AvatarDto avatarDto);
}
