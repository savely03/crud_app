package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.service.FacultyService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public FacultyDto createFaculty(@Valid @RequestBody FacultyDto facultyDto) {
        return facultyService.createFaculty(facultyDto);
    }

    @GetMapping("/{id}")
    public FacultyDto getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    @GetMapping
    public Collection<FacultyDto> getFaculties() {
        return facultyService.getFaculties();
    }

    @PutMapping("/{id}")
    public FacultyDto updateFaculty(@PathVariable Long id, @Valid @RequestBody FacultyDto facultyDto) {
        return facultyService.updateFaculty(id, facultyDto);
    }

    @DeleteMapping("/{id}")
    public FacultyDto deleteFacultyById(@PathVariable Long id) {
        return facultyService.deleteFacultyById(id);
    }

    @GetMapping("/filter")
    public FacultyDto getFacultyByNameOrColor(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String color) {
        return facultyService.getFacultyByNameOrColor(name, color);
    }

    @GetMapping("/{id}/students")
    public Collection<StudentDto> getStudentsByFacultyId(@PathVariable Long id) {
        return facultyService.getStudentsByFacultyId(id);
    }
}
