package ru.hogwarts.shared.avatar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarDto implements Serializable {
    private Long id;
    private String mediaType;
    private String url;
}
