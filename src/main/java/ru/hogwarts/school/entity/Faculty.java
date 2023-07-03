package ru.hogwarts.school.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "students")
@Builder
@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String color;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private Set<Student> students;
}
