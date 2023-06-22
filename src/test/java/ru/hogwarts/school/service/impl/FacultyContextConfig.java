package ru.hogwarts.school.service.impl;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

@TestConfiguration
public class FacultyContextConfig {

    @Bean
    public FacultyService facultyService(FacultyRepository facultyRepository) {
        return new FacultyServiceImpl(facultyRepository);
    }
}
