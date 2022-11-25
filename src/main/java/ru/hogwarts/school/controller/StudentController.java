package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.dto.FacultyDto;
import ru.hogwarts.school.model.dto.NewStudentDto;
import ru.hogwarts.school.model.dto.StudentDto;
import ru.hogwarts.school.service.StudentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Collection;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable long id) {
        return ResponseEntity.ok(studentService.getStudent(id));
    }

    @PostMapping
    public ResponseEntity<StudentDto> addStudent(@RequestBody @Valid NewStudentDto student) {
        return ResponseEntity.ok(studentService.addStudent(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable long id,
                                                    @RequestBody @Valid NewStudentDto student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void removeStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
    }

    @GetMapping(params = "age")
    public ResponseEntity<Collection<StudentDto>> getStudentsByAge(@RequestParam int age) {
        return ResponseEntity.ok(studentService.getStudentsByAge(age));
    }

    @GetMapping
    public ResponseEntity<Collection<StudentDto>> getStudentsByAgeBetween(@RequestParam int from,
                                                                          @RequestParam int to) {
        return ResponseEntity.ok(studentService.getStudentsByAgeBetween(from, to));
    }

    @GetMapping(params = "studentId")
    public ResponseEntity<FacultyDto> getFacultyByStudentId(@RequestParam Long studentId) {
        return ResponseEntity.ok(studentService.getFacultyByStudent(studentId));
    }

    @GetMapping(value = "/parallel", params = "letter")
    public ResponseEntity<Collection<String>> getAllStudentNamesStarting(@RequestParam("letter")
                                                                         @Pattern(regexp = "[a-zA-Z]", message = "Enter only one starting letter")
                                                                         String letter) {
        return ResponseEntity.ok(studentService.getAllStudentNamesStarting(letter));
    }

    @GetMapping(value = "/parallel/avg-age")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    @GetMapping("/parallel/get-unsynced")
    public ResponseEntity<Void> getInDifferentThreads(@RequestParam(value = "size", defaultValue = "6")
                                                      @Min(value = 1, message = "Minimum requested size is 1")
                                                      int size) {
        studentService.getInDifferentThreads(size);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/parallel/get-synced")
    public ResponseEntity<Void> getInDifferentThreadsSynced(@RequestParam(value = "size", defaultValue = "6")
                                                            @Min(value = 1, message = "Minimum requested size is 1")
                                                            int size) {
        studentService.getInDifferentThreadsSynced(size);
        return ResponseEntity.ok().build();
    }
}
