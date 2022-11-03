INSERT INTO student (id, name, age)
VALUES (1, 'Paul', 20) ON CONFLICT DO NOTHING;
--
INSERT INTO student (id, name, age)
VALUES (2, 'Ann', 25) ON CONFLICT DO NOTHING;
--
INSERT INTO student (id, name, age)
VALUES (3, 'Sam', 15) ON CONFLICT DO NOTHING;
--
INSERT INTO student (id, name, age)
VALUES (4, 'Victoria', 22) ON CONFLICT DO NOTHING;
--