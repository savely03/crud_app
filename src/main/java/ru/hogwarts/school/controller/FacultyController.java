package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
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
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
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
