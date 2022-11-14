package ru.hogwarts.school.mapper;

import org.junit.jupiter.api.Test;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.dto.AvatarDto;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

    private final ModelMapper out = new ModelMapper();

    @Test
    public void avatarToAvatarDto() {
        TypeMap<Avatar, AvatarDto> typeMap = out.createTypeMap(Avatar.class, AvatarDto.class);
        String port = "8080";
        String url = "http://localhost:%s/avatar/%d/from-db";
        Converter<Long, String> idToUrl = new AbstractConverter<>() {
            @Override
            protected String convert(Long source) {
                return url.formatted(port, source);
            }
        };

        typeMap.addMappings(m -> m.using(idToUrl).map(Avatar::getId, AvatarDto::setUrl));
        Avatar avatar = new Avatar(1L, "c://1/1.jpeg", 1000L, "jpeg/image", "10001010111".getBytes());
        assertThat(out.map(avatar, AvatarDto.class).getUrl()).isEqualTo("http://localhost:8080/avatar/1/from-db");
    }
}