package ru.hogwarts.school.service.impl;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.mapper.StudentMapperImpl;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

@TestConfiguration
public class StudentContextConfig {

    @Bean
    public StudentMapper studentMapper() {
        return new StudentMapperImpl();
    }
    @Bean
    public StudentService studentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        return new StudentServiceImpl(studentRepository, new FacultyServiceImpl(facultyRepository), studentMapper());
    }
}
