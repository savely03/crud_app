package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @PostMapping
    public Student createStudent(@Valid @RequestBody StudentDto studentDto) {
        return studentService.createStudent(studentMapper.toEntity(studentDto));
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping
    public Collection<Student> getStudents() {
        return studentService.getStudents();
    }

    @PutMapping
    public Student updateStudent(@Valid @RequestBody StudentDto studentDto) {
        return studentService.updateStudent(studentMapper.toEntity(studentDto));
    }

    @DeleteMapping("/{id}")
    public Student deleteStudentById(@PathVariable Long id) {
        return studentService.deleteStudentById(id);
    }

    @GetMapping("/age/{age}")
    public Collection<Student> getStudentsByAge(@PathVariable int age) {
        return studentService.getStudentsByAge(age);
    }
}
