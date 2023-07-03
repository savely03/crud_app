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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.mapper.FacultyMapperImpl;
import ru.hogwarts.school.mapper.StudentMapperImpl;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.AvatarServiceImpl;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@ExtendWith(MockitoExtension.class)
class StudentControllerWebMvcTest {

    private final static String ROOT = "/student";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private StudentServiceImpl studentService;

    @SpyBean
    private AvatarServiceImpl avatarService;

    @SpyBean
    private StudentMapperImpl studentMapper;

    @SpyBean
    private FacultyMapperImpl facultyMapper;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @InjectMocks
    private StudentController studentController;

    private static StudentDto studentDto;
    private static Student student;
    private static Faculty faculty;


    @BeforeAll
    static void init() {
        Faker faker = new Faker();
        faculty = Faculty.builder().id(1L).name(faker.harryPotter().house()).color(faker.color().name()).build();
        studentDto = StudentDto.builder().name(faker.name().firstName()).age(faker.random().nextInt(100))
                .facultyId(faculty.getId()).build();
        student = Student.builder().id(1L).name(studentDto.getName()).age(studentDto.getAge())
                .faculty(faculty).build();
    }

    @Test
    void createStudentTest() throws Exception {
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(ROOT)
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andExpect(jsonPath("$.facultyId").value(studentDto.getFacultyId()));
    }

    @Test
    void createStudentWhenFacultyDoesNotExistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(ROOT)
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentByIdTest() throws Exception {
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/" + student.getId())
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andExpect(jsonPath("$.facultyId").value(studentDto.getFacultyId()));
    }


    @Test
    void getStudentByIdWhenStudentDoesNotExistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/" + student.getId())
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsTest() throws Exception {
        when(studentRepository.findAll()).thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(student.getId()))
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()))
                .andExpect(jsonPath("$[0].facultyId").value(student.getFaculty().getId()));
    }

    @Test
    void updateStudentTest() throws Exception {
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .put(ROOT + "/" + student.getId())
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andExpect(jsonPath("$.facultyId").value(studentDto.getFacultyId()));
    }


    @Test
    void updateStudentWhenStudentDoesNotExistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put(ROOT + "/" + student.getId())
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void updateStudentWhenFacultyDoesNotExistTest() throws Exception {
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .put(ROOT + "/" + student.getId())
                        .content(objectMapper.writeValueAsString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudentByIdTest() throws Exception {
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(ROOT + "/" + student.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andExpect(jsonPath("$.facultyId").value(studentDto.getFacultyId()));

        verify(studentRepository, times(1)).findById(student.getId());
        verify(studentRepository, times(1)).deleteById(student.getId());
    }

    @Test
    void deleteStudentByIdWhenStudentDoesNotExistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(ROOT + "/" + student.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsByAgeTest() throws Exception {
        when(studentRepository.findAllByAge(student.getAge())).thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/filter")
                        .param("age", String.valueOf(student.getAge()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(student.getId()))
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()))
                .andExpect(jsonPath("$[0].facultyId").value(student.getFaculty().getId()));
    }

    @Test
    void getStudentsByAgeBetweenTest() throws Exception {
        when(studentRepository.findAllByAgeBetween(student.getAge(), student.getAge()))
                .thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/between")
                        .param("minAge", String.valueOf(student.getAge()))
                        .param("maxAge", String.valueOf(student.getAge()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(student.getId()))
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()))
                .andExpect(jsonPath("$[0].facultyId").value(student.getFaculty().getId()));
    }

    @Test
    void getFacultyByStudentIdTest() throws Exception {
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/" + student.getId() + "/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void getFacultyByStudentIdWhenStudentDoesNotExistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ROOT + "/" + student.getId() + "/faculty"))
                .andExpect(status().isNotFound());
    }

    @Test
    void uploadAvatarTest() throws Exception {
        Resource resource = new ClassPathResource("/images/supra-turbo.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", resource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                resource.getInputStream());

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, ROOT + "/" + student.getId() + "/avatar")
                        .file(multipartFile)
                        .param("id", String.valueOf(student.getId()))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(avatarRepository, times(1)).save(any(Avatar.class));

        assertThat(Files.exists(Path.of("C:/test/", student.getId() + ".jpg"))).isTrue();
    }


    @Test
    void uploadAvatarWhenStudentDoesNotExistTest() throws Exception {
        Resource resource = new ClassPathResource("/images/supra-turbo.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", resource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                resource.getInputStream());

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, ROOT + "/" + student.getId() + "/avatar")
                        .file(multipartFile)
                        .param("id", String.valueOf(student.getId()))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isNotFound());
    }
}