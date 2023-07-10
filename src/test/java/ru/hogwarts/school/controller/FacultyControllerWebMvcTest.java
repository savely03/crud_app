package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.mapper.FacultyMapperImpl;
import ru.hogwarts.school.mapper.StudentMapperImpl;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;
import ru.hogwarts.school.test_util.DbTest;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = FacultyController.class)
@ExtendWith(MockitoExtension.class)
@DbTest
class FacultyControllerWebMvcTest {

    private static final String ROOT = "/faculty";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @SpyBean
    private StudentMapperImpl studentMapper;

    @SpyBean
    private FacultyMapperImpl facultyMapper;

    @MockBean
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyController facultyController;
    private static FacultyDto facultyDto;
    private static Faculty faculty;
    private static Student student;

    private static final Faker faker = new Faker();


    @BeforeAll
    static void init() {
        student = Student.builder().id(1L).name(faker.name().firstName()).age(faker.random().nextInt(16, 100)).build();
        facultyDto = FacultyDto.builder().name(faker.harryPotter().house()).color(faker.color().name()).build();
        faculty = Faculty.builder().id(1L).name(facultyDto.getName()).color(facultyDto.getColor())
                .students(Set.of(student)).build();
    }

    @Test
    void createFacultyTest() throws Exception {
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(faculty.getName(), faculty.getColor()))
                .thenReturn(Optional.empty());
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(ROOT)
                        .content(objectMapper.writeValueAsString(facultyDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

    }

    @Test
    void createFacultyWhenFacultyAlreadyAdded() throws Exception {
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(faculty.getName(), faculty.getColor()))
                .thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(ROOT)
                        .content(objectMapper.writeValueAsString(facultyDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFacultyByIdTest() throws Exception {
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/" + faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

    }

    @Test
    void getFacultyByIdWhenFacultyDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/" + faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFacultiesTest() throws Exception {
        when(facultyRepository.findAll()).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(faculty.getId()))
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()));
    }

    @Test
    void updateFacultyTest() throws Exception {
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(ROOT + "/" + faculty.getId())
                        .content(objectMapper.writeValueAsString(facultyDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

    }

    @Test
    void updateFacultyTestWhenFacultyDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put(ROOT + "/" + faculty.getId())
                        .content(objectMapper.writeValueAsString(facultyDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFacultyByIdTest() throws Exception {
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(ROOT + "/" + faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

        verify(facultyRepository, times(1)).findById(anyLong());
        verify(facultyRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteFacultyByIdWhenFacultyDoesNotExistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(ROOT + "/" + faculty.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFacultyByNameOrColorTest() throws Exception {
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(faculty.getName(), faculty.getColor()))
                .thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/filter")
                        .param("name", faculty.getName())
                        .param("color", faculty.getColor())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void getFacultyByNameOrColorWhenFacultyDoesNotExistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/filter")
                        .param("name", faculty.getName())
                        .param("color", faculty.getColor()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsByFacultyIdTest() throws Exception {
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/" + faculty.getId() + "/students")
                        .content(objectMapper.writeValueAsString(facultyDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(student.getId()))
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()));
    }


    @Test
    void getStudentsByFacultyIdWhenFacultyDoesNotExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/" + faculty.getId() + "/students")
                        .content(objectMapper.writeValueAsString(facultyDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}