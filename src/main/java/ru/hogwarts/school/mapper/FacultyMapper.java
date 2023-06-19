package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.model.Faculty;

@Mapper
public interface FacultyMapper extends GeneralMapper<Faculty, FacultyDto> {
}
