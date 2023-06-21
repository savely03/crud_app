package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;
    private final FacultyMapper facultyMapper;

    @PostMapping
    public Faculty createFaculty(@Valid @RequestBody FacultyDto facultyDto) {
        return facultyService.createFaculty(facultyMapper.toEntity(facultyDto));
    }

    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    @GetMapping
    public Collection<Faculty> getFaculties() {
        return facultyService.getFaculties();
    }

    @PutMapping
    public Faculty updateFaculty(@Valid @RequestBody FacultyDto facultyDto) {
        return facultyService.updateFaculty(facultyMapper.toEntity(facultyDto));
    }

    @DeleteMapping("/{id}")
    public Faculty deleteFacultyById(@PathVariable Long id) {
        return facultyService.deleteFacultyById(id);
    }

    @GetMapping("/color/{color}")
    public Collection<Faculty> getFacultiesByColor(@PathVariable String color) {
        return facultyService.getFacultiesByColor(color);
    }
}
