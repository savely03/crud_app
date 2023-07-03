package ru.hogwarts.school.service;

import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;

import java.io.IOException;


public interface AvatarService {
    void uploadAvatar(Long studentId, MultipartFile multipartFile) throws IOException;

    Avatar findAvatarByStudentId(Long studentId);

    Pair<byte[], String> findAvatarByStudentIdFromDb(Long studentId);

    Pair<byte[], String> findAvatarByStudentIdFromFs(Long studentId);

}
