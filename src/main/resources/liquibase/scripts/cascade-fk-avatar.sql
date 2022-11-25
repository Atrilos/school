-- liquibase formatted sql

-- changeset atrilos:3 endDelimiter:/ rollbackEndDelimiter:/
alter table avatar
    drop constraint fkm6mgi7ty5gienen5rege7rc22;
/
-- rollback alter table avatar
-- rollback add constraint fkm6mgi7ty5gienen5rege7rc22
-- rollback foreign key (student_id) references student;
-- rollback /

-- changeset atrilos:4 endDelimiter:/ rollbackEndDelimiter:/
alter table avatar
    add constraint avatar_on_student_id_fk
        foreign key (student_id) references student
            on delete cascade;
/
-- rollback alter table avatar
-- rollback    drop constraint avatar_on_student_id_fk;
-- rollback /