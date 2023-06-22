package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.model.Student;

@Mapper
public interface StudentMapper extends GeneralMapper<Student, StudentDto> {
    @Override
    @Mapping(target = "facultyId", source = "entity.faculty.id")
    StudentDto toDto(Student entity);

    @Override
    Student toEntity(StudentDto studentDto);

}
