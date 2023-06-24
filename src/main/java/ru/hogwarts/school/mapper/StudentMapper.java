package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Student;

@Mapper
public interface StudentMapper {
    @Mapping(target = "facultyId", source = "entity.faculty.id")
    StudentDtoOut toDto(Student entity);
    Student toEntity(StudentDtoIn studentDto);
}
