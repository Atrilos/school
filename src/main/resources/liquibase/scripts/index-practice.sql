-- liquibase formatted sql

-- changeset atrilos:1
CREATE INDEX student_name_idx ON student (name);
-- rollback DROP INDEX student_name_idx;

-- changeset atrilos:2
CREATE INDEX faculty_name_color_idx ON faculty (name, color);
-- rollback DROP INDEX faculty_name_color_idx;
