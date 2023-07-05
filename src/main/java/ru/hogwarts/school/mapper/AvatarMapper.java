package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;

@Mapper
public interface AvatarMapper {
    @Mapping(target = "studentId", source = "entity.student.id")
    AvatarDto toDto(Avatar entity);
    Avatar toEntity(AvatarDto avatarDto);
}
