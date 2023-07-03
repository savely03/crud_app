package ru.hogwarts.school.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final AvatarService avatarService;

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(studentDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.getStudentById(id));
    }

    @GetMapping
    public ResponseEntity<Collection<StudentDto>> getStudents() {
        return ResponseEntity.ok().body(studentService.getStudents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) {
        return ResponseEntity.ok().body(studentService.updateStudent(id, studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudentDto> deleteStudentById(@PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.deleteStudentById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<Collection<StudentDto>> getStudentsByAge(@RequestParam int age) {
        return ResponseEntity.ok().body(studentService.getStudentsByAge(age));
    }

    @GetMapping("/between")
    public ResponseEntity<Collection<StudentDto>> getStudentsByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return ResponseEntity.ok().body(studentService.getStudentsByAgeBetween(minAge, maxAge));
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<FacultyDto> getFacultyByStudentId(@PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.getFacultyByStudentId(id));
    }


    @PatchMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadAvatar(@PathVariable Long id,
                                             @RequestParam("file") MultipartFile multipartFile
    ) throws IOException {
        avatarService.uploadAvatar(id, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
