package ru.hogwarts.school.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDto {

    @Min(value = 0, message = "Id cannot be 0")
    private long id;

    @NotBlank(message = "Name is not filled in")
    private String name;

    @NotBlank(message = "Color is not filled in")
    private String color;
}
