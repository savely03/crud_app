package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final AvatarService avatarService;

    @PostMapping
    public StudentDto createStudent(@Valid @RequestBody StudentDto studentDto) {
        return studentService.createStudent(studentDto);
    }

    @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping
    public Collection<StudentDto> getStudents() {
        return studentService.getStudents();
    }

    @PutMapping("/{id}")
    public StudentDto updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) {
        return studentService.updateStudent(id, studentDto);
    }

    @DeleteMapping("/{id}")
    public StudentDto deleteStudentById(@PathVariable Long id) {
        return studentService.deleteStudentById(id);
    }

    @GetMapping("/filter")
    public Collection<StudentDto> getStudentsByAge(@RequestParam int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("/between")
    public Collection<StudentDto> getStudentsByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.getStudentsByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public FacultyDto getFacultyByStudentId(@PathVariable Long id) {
        return studentService.getFacultyByStudentId(id);
    }


    @PostMapping(value = "{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile multipartFile) throws IOException {
        avatarService.uploadAvatar(id, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}/avatar-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatarByStudentId(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .contentLength(avatar.getData().length).body(avatar.getData());
    }


    @GetMapping("{id}/avatar")
    public ResponseEntity<Void> downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatarByStudentId(id);
        Path filePath = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(filePath);
             OutputStream os = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            bis.transferTo(bos);
            return ResponseEntity.ok().build();
        }
    }

}
