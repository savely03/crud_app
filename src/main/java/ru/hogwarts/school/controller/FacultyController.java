package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.service.FacultyService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;

    @PostMapping
    public FacultyDto createFaculty(@Valid @RequestBody FacultyDto facultyDto) {
        return facultyMapper.toDto(facultyService.createFaculty(facultyMapper.toEntity(facultyDto)));
    }

    @GetMapping("/{id}")
    public FacultyDto getFacultyById(@PathVariable Long id) {
        return facultyMapper.toDto(facultyService.getFacultyById(id));
    }

    @GetMapping
    public Collection<FacultyDto> getFaculties() {
        return facultyService.getFaculties().stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping
    public FacultyDto updateFaculty(@Valid @RequestBody FacultyDto facultyDto) {
        return facultyMapper.toDto(facultyService.updateFaculty(facultyMapper.toEntity(facultyDto)));
    }

    @DeleteMapping("/{id}")
    public FacultyDto deleteFacultyById(@PathVariable Long id) {
        return facultyMapper.toDto(facultyService.deleteFacultyById(id));
    }

    @GetMapping("/by")
    public FacultyDto getFacultyByNameOrColor(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String color) {
        return facultyMapper.toDto(facultyService.getFacultyByNameOrColor(name, color));
    }

    @GetMapping("/{id}/students")
    public Collection<StudentDto> getStudentsByFacultyId(@PathVariable Long id) {
        return facultyService.getStudentsByFacultyId(id).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }
}
