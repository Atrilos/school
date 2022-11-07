--- 1
SELECT s.name, age, f.name
FROM student s
         JOIN faculty f on f.id = s.faculty_id;
--- 2.1
SELECT s.id, age, name, faculty_id, avatar_id
FROM student s
         LEFT JOIN avatar a on a.id = s.avatar_id
WHERE a.id IS NOT NULL;
--- 2.2
SELECT s.id, age, name, faculty_id, avatar_id
FROM student s
         JOIN avatar a on a.id = s.avatar_id;