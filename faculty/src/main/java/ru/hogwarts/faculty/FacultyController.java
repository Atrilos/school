package ru.hogwarts.faculty;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.shared.faculty.Faculty;
import ru.hogwarts.shared.faculty.dto.FacultyDto;
import ru.hogwarts.shared.student.dto.StudentDto;
import ru.hogwarts.student.model.dto.FacultyDto;
import ru.hogwarts.student.model.dto.StudentDto;
import ru.hogwarts.student.service.FacultyService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
@Validated
public class FacultyController {

    private final FacultyService facultyService;

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getFacultyById(id));
    }

    @PostMapping
    public ResponseEntity<FacultyDto> addFaculty(@RequestBody @Valid FacultyDto faculty) {
        return ResponseEntity.ok(facultyService.addFaculty(faculty));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyDto> updateFaculty(@PathVariable long id,
                                                    @RequestBody @Valid FacultyDto faculty) {
        return ResponseEntity.ok(facultyService.updateFaculty(id, faculty));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFaculty(@PathVariable Long id) {
        facultyService.removeFaculty(id);
    }

    @GetMapping(params = "color")
    public ResponseEntity<Collection<FacultyDto>> getFacultiesByColor(@RequestParam @NotBlank String color) {
        return ResponseEntity.ok(facultyService.getFacultiesByColor(color));
    }

    @GetMapping(params = "name")
    public ResponseEntity<Collection<FacultyDto>> getFacultiesByName(@RequestParam @NotBlank String name) {
        return ResponseEntity.ok(facultyService.getFacultiesByName(name));
    }

    @GetMapping(params = "facultyName")
    public ResponseEntity<Collection<StudentDto>> getFacultyStudents(@RequestParam @NotBlank String facultyName) {
        return ResponseEntity.ok(facultyService.getFacultyStudents(facultyName));
    }
}
