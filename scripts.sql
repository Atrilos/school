-- 1
SELECT *
FROM student
WHERE age BETWEEN 10 and 20;
-- 2
SELECT name
FROM student;
-- 3
SELECT *
FROM student
WHERE name ILIKE '%o%';
-- 4
SELECT *
FROM student
WHERE age < id;
-- 5
SELECT *
FROM student
ORDER BY age;