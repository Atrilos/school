package ru.hogwarts.shared.avatar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.hogwarts.shared.avatar.Avatar} entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarDto implements Serializable {
    private Long id;
    private String mediaType;
    private String url;
}
