package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AvatarServiceImpl implements AvatarService {


    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Value("${path.to.avatars.folder}")
    private String avatarDir;

    @Override
    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile multipartFile) throws IOException {
        Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);
        Path filePath = Path.of(avatarDir, student.getId() + "." +
                getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = multipartFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findOrCreateAvatarByStudentId(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(multipartFile.getSize());
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setData(multipartFile.getBytes());
        avatarRepository.save(avatar);
    }

    @Override
    public Avatar findAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow(AvatarNotFoundException::new);
    }

    private Avatar findOrCreateAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
