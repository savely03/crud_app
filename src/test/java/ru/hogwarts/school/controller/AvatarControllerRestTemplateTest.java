package ru.hogwarts.school.controller;

import com.github.javafaker.Faker;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.test_util.DbTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private AvatarMapper avatarMapper;

    private static final String LOCALHOST = "http://localhost:";

    private static final String ROOT = "/avatar";

    private String baseUrl;
    private Student student;
    private final Faker faker = new Faker();
    private Faculty faculty;

    @BeforeEach
    void setUp() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        faculty = facultyRepository.save(Faculty.builder().name(faker.harryPotter().house()).color(faker.color().name()).build());
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

    static Stream<Arguments> generatePageAndSize() {
        return Stream.of(
                Arguments.of(1, 4),
                Arguments.of(2, 5),
                Arguments.of(3, 2),
                Arguments.of(4, 2),
                Arguments.of(1, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("generatePageAndSize")
    void findAllAvatarsTest(int page, int size) {
        List<AvatarDto> avatars = createAvatars().stream().map(avatarMapper::toDto).collect(Collectors.toList());

        ResponseEntity<List<AvatarDto>> responseEntity = restTemplate.exchange(
                baseUrl + "?" + "page=" + page + "&" + "size=" + size,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(avatars.subList(page * size - size, page * size));
    }


    static Stream<Arguments> generateInvalidPageAndSize() {
        return Stream.of(
                Arguments.of(0, 4),
                Arguments.of(5, 0),
                Arguments.of(0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("generateInvalidPageAndSize")
    void findAllAvatarsWhenInvalidParamsTest(int page, int size) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                baseUrl + "?" + "page=" + page + "&" + "size=" + size,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private List<Avatar> createAvatars() {
        List<Student> students = studentRepository.saveAll(Stream.generate(() ->
                        Student.builder().name(faker.name().firstName())
                                .age(faker.random().nextInt(100)).faculty(faculty).build())
                .limit(10)
                .collect(Collectors.toList()));

        return avatarRepository.saveAll(students.stream()
                .map(s -> Avatar.builder().student(s).build())
                .collect(Collectors.toList()));
    }

    @AfterEach
    void cleanUp() {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }
}