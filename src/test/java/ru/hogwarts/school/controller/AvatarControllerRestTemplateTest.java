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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.test_util.DbTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DbTest
class AvatarControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private static final String LOCALHOST = "http://localhost:";

    private static final String ROOT = "/avatar";

    private String baseUrl;
    private Student student;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        Faculty faculty = facultyRepository.save(Faculty.builder().name(faker.harryPotter().house()).color(faker.color().name()).build());
        student = Student.builder().id(1L).name(faker.name().firstName()).age(faker.random().nextInt(100))
                .faculty(faculty).build();
        baseUrl = LOCALHOST + port + ROOT;
    }

    @Test
    void downloadAvatarFromDbTest() throws IOException {
        Resource resource = new ClassPathResource("/images/supra-turbo.jpg");
        byte[] data = resource.getInputStream().readAllBytes();
        student = studentRepository.save(student);
        Avatar avatar = Avatar.builder().data(data)
                .mediaType(MediaType.MULTIPART_FORM_DATA_VALUE).student(student).fileSize(data.length).build();
        avatarRepository.save(avatar);

        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/" + student.getId() + "/db",
                byte[].class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getHeaders().getContentLength()).isEqualTo(resource.contentLength());
        assertThat(responseEntity.getBody().length).isEqualTo(avatar.getFileSize());
        assertThat(responseEntity.getBody()).isEqualTo(data);
    }

    @Test
    void downloadAvatarFromFsTest() throws IOException {
        Resource resource = new ClassPathResource("/images/supra-turbo.jpg");
        byte[] data = resource.getInputStream().readAllBytes();
        student = studentRepository.save(student);
        Avatar avatar = Avatar.builder().fileSize(data.length).filePath(resource.getFile().getPath())
                .mediaType(MediaType.MULTIPART_FORM_DATA_VALUE).student(student).build();
        avatarRepository.save(avatar);

        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/" + student.getId(),
                byte[].class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getHeaders().getContentLength()).isEqualTo(resource.contentLength());
        assertThat(responseEntity.getBody().length).isEqualTo(avatar.getFileSize());
        assertThat(responseEntity.getBody()).isEqualTo(data);
    }

    @Test
    void downloadAvatarWhenAvatarDoesNotExistTest() {
        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/" + student.getId() + "/avatar",
                byte[].class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @AfterEach
    void cleanUp() {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }
}