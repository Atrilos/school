package ru.hogwarts.shared.faculty;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "faculty",
        url = "${clients.faculty.url}"
)
public interface FacultyClient {
}
