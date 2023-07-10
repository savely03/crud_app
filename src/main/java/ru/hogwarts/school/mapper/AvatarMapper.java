package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;

@Mapper
public interface AvatarMapper {
    @Mapping(target = "studentId", source = "entity.student.id")
    @Mapping(target = "urlImage", expression = "java(setUrlImage(entity.getStudent()))")
    AvatarDto toDto(Avatar entity);

    Avatar toEntity(AvatarDto avatarDto);

    default String setUrlImage(Student student) {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .pathSegment(AvatarController.ROOT, student.getId().toString())
                .toUriString();
    }
}
