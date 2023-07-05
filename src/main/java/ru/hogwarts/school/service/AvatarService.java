package ru.hogwarts.school.service;

import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;

import java.io.IOException;
import java.util.Collection;


public interface AvatarService {
    void uploadAvatar(Long studentId, MultipartFile multipartFile) throws IOException;

    Avatar findAvatarByStudentId(Long studentId);

    Pair<byte[], String> findAvatarByStudentIdFromDb(Long studentId);

    Pair<byte[], String> findAvatarByStudentIdFromFs(Long studentId);

    Collection<AvatarDto> findAllAvatars(Integer page, Integer size);

}
