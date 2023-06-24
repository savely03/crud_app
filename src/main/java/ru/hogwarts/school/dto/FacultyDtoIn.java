package ru.hogwarts.school.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDtoIn {
    @NotBlank(message = "Name is not filled in")
    private String name;

    @NotBlank(message = "Color is not filled in")
    private String color;
}
