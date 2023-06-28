package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.entity.Faculty;

@Mapper
public interface FacultyMapper {
    FacultyDto toDto(Faculty entity);
    Faculty toEntity(FacultyDto facultyDto);
}
