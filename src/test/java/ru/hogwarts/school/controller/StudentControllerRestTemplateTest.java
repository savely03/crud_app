package ru.hogwarts.school.controller;

import com.github.javafaker.Faker;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.test_util.DbTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DbTest
public class StudentControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    private static final String LOCALHOST = "http://localhost:";

    private static final String ROOT = "/student";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private StudentMapper studentMapper;
    private StudentDto studentDto;
    private Student student;
    private Faculty faculty;

    private String baseUrl;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        faculty = facultyRepository.save(Faculty.builder().name(faker.harryPotter().house()).color(faker.color().name()).build());
        studentDto = StudentDto.builder().id(1L).name(faker.name().firstName()).age(faker.random().nextInt(16, 100)).facultyId(faculty.getId()).build();
        student = Student.builder().id(1L).name(studentDto.getName()).age(studentDto.getAge())
                .faculty(faculty).build();
        baseUrl = LOCALHOST + port + ROOT;
    }

    @Test
    void createStudentTest() {
        ResponseEntity<StudentDto> responseEntity = restTemplate.postForEntity(baseUrl, studentDto, StudentDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(studentDto);

        assertThat(studentRepository.findById(responseEntity.getBody().getId())).isPresent();

    }

    @Test
    void createStudentWhenFacultyDoesNotExistTest() {
        facultyRepository.deleteById(faculty.getId());

        ResponseEntity<StudentDto> responseEntity = restTemplate.postForEntity(baseUrl, studentDto, StudentDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getStudentByIdTest() {
        student = studentRepository.save(student);

        ResponseEntity<StudentDto> responseEntity =
                restTemplate.getForEntity(baseUrl + "/" + student.getId(), StudentDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("faculty", "facultyId")
                .isEqualTo(student);
        assertThat(responseEntity.getBody().getFacultyId()).isEqualTo(faculty.getId());
    }


    @Test
    void getStudentByIdWhenStudentDoesNotExistTest() {
        ResponseEntity<StudentDto> responseEntity =
                restTemplate.getForEntity(baseUrl + "/" + student.getId(), StudentDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getStudentsTest() {
        student = studentRepository.save(student);

        ResponseEntity<List<StudentDto>> responseEntity = restTemplate.exchange(
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
                .ignoringFields("faculty", "facultyId")
                .isEqualTo(student);
        assertThat(responseEntity.getBody().get(0).getFacultyId()).isEqualTo(faculty.getId());
    }

    @Test
    void updateStudentTest() {
        student = studentRepository.save(student);
        studentDto.setId(student.getId());

        ResponseEntity<StudentDto> responseEntity = restTemplate.exchange(
                baseUrl + "/" + studentDto.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(studentDto),
                StudentDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .isEqualTo(studentDto);
    }


    @Test
    void updateStudentWhenStudentDoesNotExistTest() {
        ResponseEntity<StudentDto> responseEntity = restTemplate.exchange(
                baseUrl + "/" + studentDto.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(studentDto),
                StudentDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    void updateStudentWhenFacultyDoesNotExistTest() {
        student = studentRepository.save(student);
        studentDto.setId(student.getId());
        facultyRepository.deleteById(faculty.getId());

        ResponseEntity<StudentDto> responseEntity = restTemplate.exchange(
                baseUrl + "/" + studentDto.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(studentDto),
                StudentDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteStudentByIdTest() {
        student = studentRepository.save(student);

        ResponseEntity<StudentDto> responseEntity = restTemplate.exchange(
                baseUrl + "/" + student.getId(),
                HttpMethod.DELETE,
                null,
                StudentDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("faculty", "facultyId")
                .isEqualTo(student);
        assertThat(responseEntity.getBody().getFacultyId()).isEqualTo(faculty.getId());
    }

    @Test
    void deleteStudentByIdWhenStudentDoesNotExistTest() {
        ResponseEntity<StudentDto> responseEntity = restTemplate.exchange(
                baseUrl + "/" + student.getId(),
                HttpMethod.DELETE,
                null,
                StudentDto.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getStudentsByAgeTest() {
        student = studentRepository.save(student);

        ResponseEntity<List<StudentDto>> responseEntity = restTemplate.exchange(
                baseUrl + "/filter" + "?" + "age=" + student.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("faculty", "facultyId")
                .isEqualTo(student);
        assertThat(responseEntity.getBody().get(0).getFacultyId()).isEqualTo(faculty.getId());

    }

    @Test
    void getStudentsByAgeBetweenTest() {
        student = studentRepository.save(student);

        ResponseEntity<List<StudentDto>> responseEntity = restTemplate.exchange(
                baseUrl + "/between" + "?" + "minAge=" + student.getAge() + "&" + "maxAge=" + student.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("faculty", "facultyId")
                .isEqualTo(student);
        assertThat(responseEntity.getBody().get(0).getFacultyId()).isEqualTo(faculty.getId());
    }

    @Test
    void getFacultyByStudentIdTest() {
        student = studentRepository.save(student);

        ResponseEntity<FacultyDto> responseEntity =
                restTemplate.getForEntity(baseUrl + "/" + student.getId() + "/faculty", FacultyDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("students")
                .isEqualTo(faculty);
    }

    @Test
    void getFacultyByStudentIdWhenStudentDoesNotExistTest() {
        ResponseEntity<FacultyDto> responseEntity =
                restTemplate.getForEntity(baseUrl + "/" + student.getId() + "/faculty", FacultyDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void uploadAvatarTest() {
        student = studentRepository.save(student);
        Resource resource = new ClassPathResource("/images/supra-turbo.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.MULTIPART_FORM_DATA_VALUE));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Void> responseEntity =
                restTemplate.exchange(
                        baseUrl + "/" + student.getId() + "/avatar",
                        HttpMethod.PATCH,
                        entity,
                        Void.class
                );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(avatarRepository.findByStudentId(student.getId())).isPresent();
        assertThat(Files.exists(Path.of("C:/test/" + student.getId() + ".jpg"))).isTrue();

    }


    @Test
    void uploadAvatarWhenStudentDoesNotExistTest() {
        Resource resource = new ClassPathResource("/images/supra-turbo.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.MULTIPART_FORM_DATA_VALUE));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Void> responseEntity =
                restTemplate.exchange(
                        baseUrl + "/" + student.getId() + "/avatar",
                        HttpMethod.PATCH,
                        entity,
                        Void.class
                );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getCountOfStudentsTest() {
        List<Student> students = createStudents();

        ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(baseUrl + "/count", Integer.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(students.size());
    }

    @Test
    void getAvgAgeOfStudentsTest() {
        List<Student> students = createStudents();
        double avg = students.stream().mapToInt(Student::getAge).sum() / (double) students.size();

        ResponseEntity<Double> responseEntity = restTemplate.getForEntity(baseUrl + "/avg-age", Double.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(avg);
    }

    @Test
    void getLastFiveStudents() {
        List<Student> students = createStudents();
        List<StudentDto> lastFiveStudents = students.subList(students.size() - 5, students.size())
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());

        ResponseEntity<List<StudentDto>> responseEntity = restTemplate.exchange(
                baseUrl + "/last/five",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).hasSize(5);
        assertThat(responseEntity.getBody()).isEqualTo(lastFiveStudents);
    }


    private List<Student> createStudents() {
        return studentRepository.saveAll(Stream.generate(() ->
                        Student.builder().name(faker.name().firstName()).faculty(faculty)
                                .age(faker.random().nextInt(16, 100)).build())
                .limit(10).collect(Collectors.toList()));
    }

    @AfterEach
    void cleanUp() {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }
}
