package ru.hogwarts.school.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "students")
@Builder
@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String color;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private Collection<Student> students;

    public void addStudent(Student student) {
        if (Objects.isNull(students)) {
            students = new ArrayList<>();
        }
        students.add(student);
        student.setFaculty(this);
    }
}
