package ru.hogwarts.school.mapper;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.dto.AvatarDto;

@Configuration
public class MapperConfiguration {

    @Value("${server.port}")
    private String port;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        addMappingsAvatarToAvatarDto(mapper);
        return mapper;
    }

    private void addMappingsAvatarToAvatarDto(ModelMapper mapper) {
        TypeMap<Avatar, AvatarDto> typeMap = mapper.createTypeMap(Avatar.class, AvatarDto.class);
        Converter<Long, String> idToUrl = new AbstractConverter<>() {
            @Override
            protected String convert(Long source) {
                return "http://localhost:%s/avatar/%d/from-db".formatted(port, source);
            }
        };
        typeMap.addMappings(m -> m.using(idToUrl).map(Avatar::getId, AvatarDto::setUrl));
    }
}
