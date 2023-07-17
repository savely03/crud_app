package ru.hogwarts.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Faculty;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByNameIgnoreCaseAndColorIgnoreCase(String name, String color);

    @Query("SELECT f.name FROM Faculty f ORDER BY length(f.name) DESC")
    Page<String> findLongestFacultyName(Pageable pageable);
}
