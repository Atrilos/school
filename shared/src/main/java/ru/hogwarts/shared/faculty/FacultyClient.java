package ru.hogwarts.shared.faculty;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "faculty",
        url = "${clients.faculty.url}"
)
public interface FacultyClient {
    @GetMapping("faculty/{id}")
    ResponseEntity<Faculty> getFaculty(@PathVariable Long id);
}
