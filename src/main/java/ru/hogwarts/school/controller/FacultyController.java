package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<FacultyDto> createFaculty(@Valid @RequestBody FacultyDto facultyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facultyService.createFaculty(facultyDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyDto> getFacultyById(@PathVariable Long id) {
        return ResponseEntity.ok().body(facultyService.getFacultyById(id));
    }

    @GetMapping
    public ResponseEntity<Collection<FacultyDto>> getFaculties() {
        return ResponseEntity.ok().body(facultyService.getFaculties());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyDto> updateFaculty(@PathVariable Long id, @Valid @RequestBody FacultyDto facultyDto) {
        return ResponseEntity.ok().body(facultyService.updateFaculty(id, facultyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FacultyDto> deleteFacultyById(@PathVariable Long id) {
        return ResponseEntity.ok().body(facultyService.deleteFacultyById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<FacultyDto> getFacultyByNameOrColor(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) String color) {
        return ResponseEntity.ok().body(facultyService.getFacultyByNameAndColor(name, color));
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Collection<StudentDto>> getStudentsByFacultyId(@PathVariable Long id) {
        return ResponseEntity.ok().body(facultyService.getStudentsByFacultyId(id));
    }

    @GetMapping("/name/longest")
    public String findLongestFacultyName() {
        return facultyService.findLongestFacultyName();
    }
}
