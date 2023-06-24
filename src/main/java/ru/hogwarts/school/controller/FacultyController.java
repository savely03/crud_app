package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.service.FacultyService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public FacultyDtoOut createFaculty(@Valid @RequestBody FacultyDtoIn facultyDtoIn) {
        return facultyService.createFaculty(facultyDtoIn);
    }

    @GetMapping("/{id}")
    public FacultyDtoOut getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    @GetMapping
    public Collection<FacultyDtoOut> getFaculties() {
        return facultyService.getFaculties();
    }

    @PutMapping("/{id}")
    public FacultyDtoOut updateFaculty(@PathVariable Long id, @Valid @RequestBody FacultyDtoIn facultyDtoIn) {
        return facultyService.updateFaculty(id, facultyDtoIn);
    }

    @DeleteMapping("/{id}")
    public FacultyDtoOut deleteFacultyById(@PathVariable Long id) {
        return facultyService.deleteFacultyById(id);
    }

    @GetMapping("/filter")
    public FacultyDtoOut getFacultyByNameOrColor(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String color) {
        return facultyService.getFacultyByNameOrColor(name, color);
    }

    @GetMapping("/{id}/students")
    public Collection<StudentDtoOut> getStudentsByFacultyId(@PathVariable Long id) {
        return facultyService.getStudentsByFacultyId(id);
    }
}
