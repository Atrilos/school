package ru.hogwarts.school.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.hogwarts.school.model.Avatar} entity
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
