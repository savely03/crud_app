package ru.hogwarts.school.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvatarDto {
    private Long id;
    private Long studentId;
    private String urlImage;
}
