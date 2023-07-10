package ru.hogwarts.school.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@Builder
public class FacultyDto {

    private Long id;

    @NotBlank(message = "Name is not filled in")
    private String name;

    @NotBlank(message = "Color is not filled in")
    private String color;
}
