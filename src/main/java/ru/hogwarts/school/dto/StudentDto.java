package ru.hogwarts.school.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class StudentDto {

    private long id;

    @NotBlank(message = "Name is not filled in")
    private String name;

    @Min(value = 0, message = "Age cannot be negative")
    private int age;

    @Min(value = 1)
    private long facultyId;

}
