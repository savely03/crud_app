package ru.hogwarts.school.mapper;

import org.mapstruct.Mapper;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.model.Student;

@Mapper
public interface StudentMapper extends GeneralMapper<Student, StudentDto> {
}
