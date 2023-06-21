package ru.hogwarts.school.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FacultyDto {
    @NotBlank(message = "Name is not filled in")
    private String name;

    @NotBlank(message = "Color is not filled in")
    private String color;
}
