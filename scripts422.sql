DROP TABLE person;

DROP TABLE car;

CREATE TABLE IF NOT EXISTS person
(
    person_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255),
    has_license BOOLEAN DEFAULT 'false',
    car_id      BIGINT,
    CONSTRAINT pk_person PRIMARY KEY (person_id)
);

CREATE TABLE IF NOT EXISTS car
(
    car_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    brand  VARCHAR(255),
    model  VARCHAR(255),
    cost   NUMERIC(16, 2),
    CONSTRAINT pk_car PRIMARY KEY (car_id)
);

ALTER TABLE person
    ADD CONSTRAINT fk_person_on_car FOREIGN KEY (car_id) REFERENCES car
        ON DELETE SET NULL;

--- test

INSERT INTO car (brand, model, cost)
VALUES ('A', 'A1', 100.0);

INSERT INTO person (name, car_id)
VALUES ('Will', 1);

delete
from car
where car_id = 1;