package ru.hogwarts.school.controller;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.test_util.DbTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DbTest
public class FacultyControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    private static final String LOCALHOST = "http://localhost:";

    private static final String ROOT = "/faculty";

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;


    private FacultyDto facultyDto;
    private Faculty faculty;
    private Student student;

    private String baseUrl;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        student = Student.builder().id(1L).name(faker.name().firstName()).age(faker.random().nextInt(16, 100)).build();
        facultyDto = FacultyDto.builder().name(faker.harryPotter().house()).color(faker.color().name()).build();
        faculty = Faculty.builder().id(1L).name(facultyDto.getName()).color(facultyDto.getColor()).build();
        baseUrl = LOCALHOST + port + ROOT;
    }

    @Test
    void createFacultyTestTest() {
        ResponseEntity<FacultyDto> responseEntity = restTemplate.postForEntity(baseUrl, facultyDto, FacultyDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(facultyDto);
        assertThat(facultyRepository.findById(responseEntity.getBody().getId())).isPresent();
    }


    @Test
    void createFacultyWhenFacultyAlreadyAdded() {
        facultyRepository.save(faculty);

        ResponseEntity<FacultyDto> responseEntity = restTemplate.postForEntity(baseUrl, facultyDto, FacultyDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getFacultyByIdTest() {
        faculty = facultyRepository.save(faculty);

        ResponseEntity<FacultyDto> responseEntity =
                restTemplate.getForEntity(baseUrl + "/" + faculty.getId(), FacultyDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("students")
                .isEqualTo(faculty);
    }

    @Test
    void getFacultyByIdWhenFacultyDoesNotExist() {
        ResponseEntity<FacultyDto> responseEntity =
                restTemplate.getForEntity(baseUrl + "/" + faculty.getId(), FacultyDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getFacultiesTest() {
        faculty = facultyRepository.save(faculty);

        ResponseEntity<List<FacultyDto>> responseEntity = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("students")
                .isEqualTo(faculty);
    }

    @Test
    void updateFacultyTest() {
        faculty = facultyRepository.save(faculty);
        facultyDto.setId(faculty.getId());

        ResponseEntity<FacultyDto> responseEntity = restTemplate.exchange(
                baseUrl + "/" + faculty.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(facultyDto),
                FacultyDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(facultyDto);
        assertThat(facultyRepository.findById(faculty.getId()))
                .usingRecursiveComparison()
                .ignoringFields("students")
                .isNotEqualTo(faculty);
    }

    @Test
    void updateFacultyTestWhenFacultyDoesNotExist() {
        ResponseEntity<FacultyDto> responseEntity = restTemplate.exchange(
                baseUrl + "/" + faculty.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(facultyDto),
                FacultyDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void deleteFacultyByIdTest() {
        faculty = facultyRepository.save(faculty);

        ResponseEntity<FacultyDto> responseEntity = restTemplate.exchange(
                baseUrl + "/" + faculty.getId(),
                HttpMethod.DELETE,
                null,
                FacultyDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("students")
                .isEqualTo(faculty);
    }

    @Test
    void deleteFacultyByIdWhenFacultyDoesNotExistTest() {
        ResponseEntity<FacultyDto> responseEntity = restTemplate.exchange(
                baseUrl + "/" + faculty.getId(),
                HttpMethod.DELETE,
                null,
                FacultyDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getFacultyByNameOrColorTest() {
        faculty = facultyRepository.save(faculty);

        ResponseEntity<FacultyDto> responseEntity = restTemplate.getForEntity(
                baseUrl + "/filter" + "?" + "name=" + faculty.getName() + "&" + "color=" + faculty.getColor(),
                FacultyDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("students")
                .isEqualTo(faculty);
    }

    @Test
    void getFacultyByNameOrColorWhenFacultyDoesNotExistTest() {
        ResponseEntity<FacultyDto> responseEntity = restTemplate.getForEntity(
                baseUrl + "/filter" + "?" + "name=" + faculty.getName() + "&" + "color=" + faculty.getColor(),
                FacultyDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getStudentsByFacultyIdTest() {
        faculty = facultyRepository.save(faculty);
        student.setFaculty(faculty);
        student = studentRepository.save(student);

        ResponseEntity<List<StudentDto>> responseEntity = restTemplate.exchange(
                baseUrl + "/" + faculty.getId() + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(student)
                .usingRecursiveComparison()
                .ignoringFields("faculty")
                .isEqualTo(responseEntity.getBody().get(0));
        assertThat(responseEntity.getBody().get(0).getFacultyId()).isEqualTo(faculty.getId());

    }

    @Test
    void getStudentsByFacultyIdWhenFacultyDoesNotExist() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                baseUrl + "/" + faculty.getId() + "/students", String.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @AfterEach
    void cleanUp() {
        facultyRepository.deleteAll();
        studentRepository.deleteAll();
    }
}
