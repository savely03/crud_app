package ru.hogwarts.school.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int age;

    @ManyToOne(optional = false)
    @JoinColumn(name = "faculty_id", referencedColumnName = "id")
    private Faculty faculty;

}
