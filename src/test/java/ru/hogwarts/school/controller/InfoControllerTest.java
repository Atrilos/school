package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

class InfoControllerTest {
    private final InfoController out = new InfoController();

    @Test
    void shouldReturnSameResultUsingGauss() {
        long expected = LongStream.iterate(1, a -> a + 1).limit(1_000_000).reduce(0, Long::sum);
        assertThat(out.getSum()).isEqualTo(expected);
    }
}