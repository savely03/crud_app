package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
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
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
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
