package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Student;

import java.util.Collection;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findAllByAge(int age);

    Collection<Student> findAllByAgeBetween(int minAge, int maxAge);

    @Query(value = "SELECT COUNT(s) FROM Student s")
    int getCountOfStudents();

    @Query(value = "SELECT AVG(s.age) FROM Student s")
    double getAvgAgeOfStudents();

    @Query(value = "SELECT * FROM (SELECT * FROM Student ORDER BY id DESC LIMIT 5) a ORDER BY id", nativeQuery = true)
    Collection<Student> getLastFiveStudents();

    @Query("SELECT upper(s.name) FROM Student s WHERE upper(s.name) LIKE concat(:startWith, '%') ORDER BY s.name")
    Collection<String> findAllSortUpperNamesStartingWith(@Param("startWith") String startWith);

}
