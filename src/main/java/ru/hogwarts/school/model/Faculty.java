package ru.hogwarts.school.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Faculty {
    private Long id;
    private String name;
    private String color;
}
