package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
        return "http://localhost:8080/avatar/" + student.getId();
    }
}
