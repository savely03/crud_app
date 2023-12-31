package ru.hogwarts.school.controller;

import com.github.javafaker.Faker;
import org.hamcrest.Matchers;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.PaginationException;
import ru.hogwarts.school.mapper.AvatarMapperImpl;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.AvatarServiceImpl;
import ru.hogwarts.school.test_util.DbTest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AvatarController.class)
@ExtendWith(MockitoExtension.class)
@DbTest
public class AvatarControllerWebMvcTest {

    private static final String ROOT = "/avatar";

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AvatarServiceImpl avatarService;

    @SpyBean
    private AvatarMapperImpl avatarMapper;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private StudentRepository studentRepository;

    @InjectMocks
    private AvatarController avatarController;

    private static Student student;

    private static Avatar avatar;

    @BeforeAll
    static void setUp() throws IOException {
        Resource resource = new ClassPathResource("images/supra-turbo.jpg");
        Faker faker = new Faker();
        student = Student.builder().id(1L).name(faker.name().firstName()).age(faker.random().nextInt(16, 100)).build();
        avatar = Avatar.builder().id(1L).student(student).mediaType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .filePath(resource.getFile().getPath()).fileSize(resource.contentLength())
                .data(resource.getInputStream().readAllBytes()).build();
    }

    @Test
    void downloadAvatarFromDbTest() throws Exception {
        when(studentRepository.findById(eq(student.getId()))).thenReturn(Optional.of(student));
        when(avatarRepository.findByStudentId(eq(student.getId()))).thenReturn(Optional.of(avatar));

        mockMvc.perform(MockMvcRequestBuilders.get(ROOT + "/{id}/db", student.getId())
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(result -> {
                            MockHttpServletResponse response = result.getResponse();
                            assertThat(response.getContentType()).isEqualTo(MediaType.MULTIPART_FORM_DATA_VALUE);
                            assertThat(response.getContentLength()).isEqualTo(avatar.getFileSize());
                            assertThat(response.getContentAsByteArray()).isEqualTo(avatar.getData());
                        }
                );
    }

    @Test
    void downloadAvatarFromFsTest() throws Exception {
        when(studentRepository.findById(eq(student.getId()))).thenReturn(Optional.of(student));
        when(avatarRepository.findByStudentId(eq(student.getId()))).thenReturn(Optional.of(avatar));

        mockMvc.perform(MockMvcRequestBuilders.get(ROOT + "/{id}", student.getId())
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(result -> {
                            MockHttpServletResponse response = result.getResponse();
                            assertThat(response.getContentType()).isEqualTo(MediaType.MULTIPART_FORM_DATA_VALUE);
                            assertThat(response.getContentLength()).isEqualTo(avatar.getFileSize());
                            assertThat(response.getContentAsByteArray()).isEqualTo(avatar.getData());
                        }
                );
    }

    @Test
    void downloadAvatarWhenAvatarDoesNotExistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROOT + "/{id}", student.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertThat(result.getResolvedException().getClass()).isEqualTo(AvatarNotFoundException.class));
    }

    @Test
    void findAllAvatarsTest() throws Exception {
        Avatar secondAvatar = Avatar.builder().id(2L).student(student).build();
        int page = 1, size = 2;
        PageRequest pageRequest = PageRequest.of(0, size);
        when(avatarRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(avatar, secondAvatar)));

        mockMvc.perform(MockMvcRequestBuilders.get(ROOT)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(avatar.getId()))
                .andExpect(jsonPath("$[0].studentId").value(student.getId()))
                .andExpect(jsonPath("$[1].id").value(secondAvatar.getId()))
                .andExpect(jsonPath("$[1].studentId").value(student.getId()));
    }

    @Test
    void findAllAvatarsWhenInvalidParamsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROOT)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(2))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException().getClass()).isEqualTo(PaginationException.class));

        mockMvc.perform(MockMvcRequestBuilders.get(ROOT)
                        .param("page", String.valueOf(5))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException().getClass()).isEqualTo(PaginationException.class));
    }
}
