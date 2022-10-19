package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getFacultyById(id));
    }

    @PostMapping
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.addFaculty(faculty));
    }

    @PutMapping
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.updateFaculty(faculty));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> removeFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.removeFaculty(id));
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getFacultiesByColor(@RequestParam String color) {
        return ResponseEntity.ok(facultyService.getFacultiesByColor(color));
    }
}