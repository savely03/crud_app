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
public class StudentDtoIn {

    @NotBlank(message = "Name is not filled in")
    private String name;

    @Min(value = 0, message = "Age cannot be negative")
    private int age;

    @Min(value = 1, message = "Faculty not found")
    private long facultyId;
}
