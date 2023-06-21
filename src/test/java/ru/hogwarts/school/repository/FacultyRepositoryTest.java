package ru.hogwarts.school.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;
import static ru.hogwarts.school.constants.FacultyConstants.*;

@DataJpaTest
@AutoConfigureTestDatabase
class FacultyRepositoryTest {

    @Autowired
    private FacultyRepository out;

    @Test
    void findAllByColorTest() {
        out.save(FACULTY1);

        assertThat(out.findAllByColor(FACULTY1.getColor())).containsOnly(FACULTY1);
    }
}