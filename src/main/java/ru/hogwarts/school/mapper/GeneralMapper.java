package ru.hogwarts.school.mapper;

public interface GeneralMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
