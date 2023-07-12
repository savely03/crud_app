package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.PaginationException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final AvatarMapper avatarMapper;

    Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    @Value("${path.to.avatars.folder}")
    private String avatarDir;

    private Avatar findOrCreateAvatarByStudentId(Long studentId) {
        logger.info("Was invoked method for finding or creating avatar");
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    @Override
    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile multipartFile) throws IOException {
        logger.info("Was invoked method for uploading avatar");
        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            logger.warn("Student doesn't exist");
            return new StudentNotFoundException();
        });

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

    @Override
    public Pair<byte[], String> findAvatarByStudentIdFromDb(Long studentId) {
        Avatar avatar = findAvatarByStudentId(studentId);
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    @SneakyThrows
    @Override
    public Pair<byte[], String> findAvatarByStudentIdFromFs(Long studentId) {
        Avatar avatar = findAvatarByStudentId(studentId);
        return Pair.of(Files.readAllBytes(Path.of(avatar.getFilePath())), avatar.getMediaType());
    }

    @Override
    public Avatar findAvatarByStudentId(Long studentId) {
        logger.info("Was invoked method for finding avatar");
        return avatarRepository.findByStudentId(studentId).orElseThrow(() -> {
            logger.warn("Avatar doesn't exist");
            return new AvatarNotFoundException();
        });
    }

    @Override
    public Collection<AvatarDto> findAllAvatars(Integer page, Integer size) {
        logger.info("Was invoked method for finding all avatars");
        if (page <= 0 || size <= 0) {
            logger.warn("Page or size are incorrect");
            throw new PaginationException();
        }
        return avatarRepository.findAll(PageRequest.of(page - 1, size))
                .getContent()
                .stream()
                .map(avatarMapper::toDto)
                .collect(Collectors.toList());
    }

}
