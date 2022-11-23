package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/getPort")
    public ResponseEntity<String> getPort() {
        return ResponseEntity.ok(port);
    }

    /**
     * Параллельное выполнение не ускоряет выполнение метода, а наоборот замедляет, из-за оптимизаций компилятора,
     * а также долгого времени создания потоков в ForkJoin (во всяком случае в Windows).
     * Лучшее решение для ускорения будет использование формулы Гаусса, но опять же выполнение будет последовательным.
     */
    @GetMapping("/parallel")
    public Long getSum() {
        return (long) 1_000_000 * 1_000_001 / 2;
    }
}
