package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.entity.Student;

@Mapper
public interface StudentMapper {
    @Mapping(target = "facultyId", source = "entity.faculty.id")
    StudentDto toDto(Student entity);
    Student toEntity(StudentDto studentDto);
}
