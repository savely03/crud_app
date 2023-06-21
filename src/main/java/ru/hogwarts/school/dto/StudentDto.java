package ru.hogwarts.school.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class StudentDto {
    @NotBlank(message = "Name is not filled in")
    private String name;

    @Min(value = 0, message = "Age cannot be negative")
    private int age;

}
