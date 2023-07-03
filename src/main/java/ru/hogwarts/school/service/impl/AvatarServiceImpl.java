package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

        Path filePath = Path.of(avatarDir,
                student.getId() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        Files.write(filePath, multipartFile.getBytes());

        Avatar avatar = findOrCreateAvatarByStudentId(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(multipartFile.getSize());
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setData(multipartFile.getBytes());
        avatarRepository.save(avatar);
    }

    public Pair<byte[], String> findAvatarByStudentIdFromDb(Long studentId) {
        Avatar avatar = findAvatarByStudentId(studentId);
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    @SneakyThrows
    public Pair<byte[], String> findAvatarByStudentIdFromFs(Long studentId) {
        Avatar avatar = findAvatarByStudentId(studentId);
        return Pair.of(Files.readAllBytes(Path.of(avatar.getFilePath())), avatar.getMediaType());
    }

    @Override
    public Avatar findAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow(AvatarNotFoundException::new);
    }

    private Avatar findOrCreateAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

}
