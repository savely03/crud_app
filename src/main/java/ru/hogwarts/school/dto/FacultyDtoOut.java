package ru.hogwarts.school.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDtoOut {
    private long id;

    private String name;

    private String color;
}
