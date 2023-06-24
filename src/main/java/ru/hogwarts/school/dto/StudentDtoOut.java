package ru.hogwarts.school.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDtoOut {
    private long id;

    private String name;

    private int age;

    private long facultyId;
}
