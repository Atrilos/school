package ru.hogwarts.shared.student;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "student",
        url = "${clients.student.url}"
)
public class StudentClient {
}
