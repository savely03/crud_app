package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.entity.Faculty;

@Mapper
public interface FacultyMapper {
    FacultyDtoOut toDto(Faculty entity);
    Faculty toEntity(FacultyDtoIn entity);
}
