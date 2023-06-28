package ru.hogwarts.school.service.impl;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.test_util.DbTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@DbTest
class AvatarServiceImplTest {

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private static MockMultipartFile multipartFile;

    @Autowired
    private AvatarService avatarService;

    @Value("${path.to.avatars.folder}")
    private String testDir;

    private Student student;

    @BeforeAll
    static void init() throws IOException {
        Resource resource = new ClassPathResource("/images/supra-turbo.jpg");
        multipartFile = new MockMultipartFile(
                "supra", resource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                resource.getInputStream());
    }

    @BeforeEach
    void setUp() {
        Faculty faculty = facultyRepository.save(Faculty.builder().name("faculty").color("red").build());
        student = studentRepository.save(Student.builder().name("student").age(21).faculty(faculty).build());
    }

    @Test
    void uploadAvatarTest() throws IOException {
        avatarService.uploadAvatar(student.getId(), multipartFile);

        Avatar avatar = avatarService.findAvatarByStudentId(student.getId());

        assertThat(avatar.getData()).isEqualTo(multipartFile.getBytes());
        assertThat(avatar.getMediaType()).isEqualTo(multipartFile.getContentType());
        assertThat(avatar.getFileSize()).isEqualTo(multipartFile.getSize());
        assertThat(avatar.getStudent()).isEqualTo(student);
        assertThat(Files.exists(Path.of(avatar.getFilePath()))).isTrue();
    }

    @Test
    void uploadAvatarWhenStudentDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> avatarService.uploadAvatar(student.getId() + 1, multipartFile)
        );

    }

    @Test
    void findAvatarByStudentIdTest() throws IOException {
        avatarService.uploadAvatar(student.getId(), multipartFile);

        Avatar avatar = avatarService.findAvatarByStudentId(student.getId());

        assertThat(avatar.getData()).isEqualTo(multipartFile.getBytes());
        assertThat(avatar.getMediaType()).isEqualTo(multipartFile.getContentType());
        assertThat(avatar.getFileSize()).isEqualTo(multipartFile.getSize());
        assertThat(avatar.getStudent()).isEqualTo(student);
        assertThat(Files.exists(Path.of(avatar.getFilePath()))).isTrue();
    }

    @Test
    void findAvatarByStudentIdWhenAvatarDoesNotExist() {
        assertThatExceptionOfType(AvatarNotFoundException.class).isThrownBy(
                () -> avatarService.findAvatarByStudentId(student.getId() + 1)
        );
    }

    @AfterEach
    void cleanUp() throws IOException {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
        FileUtils.deleteDirectory(new File(testDir));
    }
}