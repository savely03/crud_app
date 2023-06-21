package ru.hogwarts.school.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.*;
import static ru.hogwarts.school.constants.StudentConstants.*;


@DataJpaTest
@AutoConfigureTestDatabase
class StudentRepositoryTest {

    @Autowired
    private StudentRepository out;

    @Test
    void findAllByAge() {
        out.save(STUDENT1);

        assertThat(out.findAllByAge(STUDENT1.getAge())).containsOnly(STUDENT1);
    }
}