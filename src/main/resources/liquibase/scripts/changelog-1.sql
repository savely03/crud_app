-- liquibase formatted sql

-- changeset SavelyDomnikov:1
CREATE TABLE faculty
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(50) NOT NULL,
    color VARCHAR(50) NOT NULL,
    UNIQUE (name, color)
);

-- changeset SavelyDomnikov:2
CREATE TABLE student
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    age        int         NOT NULL CHECK ( age >= 16 ) DEFAULT 20,
    faculty_id BIGINT      NOT NULL REFERENCES faculty (id)
);

-- changeset SavelyDomnikov:3
CREATE TABLE avatar
(
    id         BIGSERIAL PRIMARY KEY,
    file_path  TEXT,
    file_size  BIGINT,
    media_type VARCHAR(50),
    data       bytea,
    student_id BIGINT NOT NULL REFERENCES student (id)
);

-- changeset SavelyDomnikov:4
CREATE INDEX student_name_index ON student (name);

-- changeset SavelyDomnikov:5
CREATE INDEX faculty_name_and_color_index ON faculty (name, color)

