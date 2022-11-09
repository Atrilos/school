package ru.hogwarts.avatar;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.hogwarts.shared.avatar.AvatarDto;

@Component
public class AvatarMapper {
    private final ModelMapper modelMapper = new ModelMapper();
    @Value("${server.port}")
    private String port;

    public AvatarDto toDto(Avatar avatar) {
        AvatarDto avatarDto = modelMapper.map(avatar, AvatarDto.class);
        avatarDto.setUrl("http://localhost:%s/avatar/%d/from-db".formatted(port, avatar.getId()));
        return avatarDto;
    }

}
