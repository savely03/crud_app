package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.service.StudentService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;

    @PostMapping
    public StudentDto createStudent(@Valid @RequestBody StudentDto studentDto) {
        return studentMapper.toDto(studentService.createStudent(studentDto));
    }

    @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable Long id) {
        return studentMapper.toDto(studentService.getStudentById(id));
    }

    @GetMapping
    public Collection<StudentDto> getStudents() {
        return studentService.getStudents().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping
    public StudentDto updateStudent(@Valid @RequestBody StudentDto studentDto) {
        return studentMapper.toDto(studentService.updateStudent(studentDto));
    }

    @DeleteMapping("/{id}")
    public StudentDto deleteStudentById(@PathVariable Long id) {
        return studentMapper.toDto(studentService.deleteStudentById(id));
    }

    @GetMapping("/by")
    public Collection<StudentDto> getStudentsByAge(@RequestParam int age) {
        return studentService.getStudentsByAge(age).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/between")
    public Collection<StudentDto> getStudentsByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.getStudentsByAgeBetween(minAge, maxAge).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/faculty")
    public FacultyDto getFacultyByStudentId(@PathVariable Long id) {
        return facultyMapper.toDto(studentService.getFacultyByStudentId(id));
    }
}
