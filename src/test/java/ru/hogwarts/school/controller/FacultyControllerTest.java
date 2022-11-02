package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.hogwarts.school.controller.constants.FacultyControllerTestConstants.*;
import static ru.hogwarts.school.controller.constants.StudentControllerTestConstants.BASIC_STUDENT;
import static ru.hogwarts.school.controller.constants.StudentControllerTestConstants.BASIC_STUDENT_2;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson GSON = new Gson();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private FacultyService facultyService;
    private Faculty basicFaculty;

    @BeforeEach
    public void setUp() {
        final long id = 1L;
        final String name = "Rd";
        final String color = "Red";
        basicFaculty = Faculty.builder()
                .id(id)
                .name(name)
                .color(color)
                .build();
    }

    @Test
    public void shouldReturnCorrectFaculty_IfGet() throws Exception {
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(basicFaculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", basicFaculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(basicFaculty.getId()))
                .andExpect(jsonPath("$.name").value(basicFaculty.getName()))
                .andExpect(jsonPath("$.color").value(basicFaculty.getColor()));
    }

    @Test
    public void shouldThrow_IfFacultyIdDoesntExist() throws Exception {
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", basicFaculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertThat(result.getResolvedException()).isInstanceOf(EntryNotFoundException.class));
    }

    @Test
    public void shouldAddFacultyCorrectly() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(basicFaculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(objectMapper.writeValueAsString(basicFaculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(basicFaculty.getId()))
                .andExpect(jsonPath("$.name").value(basicFaculty.getName()))
                .andExpect(jsonPath("$.color").value(basicFaculty.getColor()));
    }

    @Test
    public void shouldUpdateFacultyCorrectly() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(basicFaculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(objectMapper.writeValueAsString(basicFaculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(basicFaculty.getId()))
                .andExpect(jsonPath("$.name").value(basicFaculty.getName()))
                .andExpect(jsonPath("$.color").value(basicFaculty.getColor()));
    }

    @Test
    public void shouldGetCorrectFacultiesByColor() throws Exception {
        List<Faculty> resultBlue = List.of(FACULTY_1, FACULTY_2, FACULTY_3);
        List<Faculty> resultRed = List.of(basicFaculty);
        when(facultyRepository.findFacultiesByColor(eq("Red"))).thenReturn(resultRed);
        when(facultyRepository.findFacultiesByColor(eq("Blue"))).thenReturn(resultBlue);
        String res = mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color={color}", "Blue")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(FACULTY_1.getId()))
                .andExpect(jsonPath("$[0].name").value(FACULTY_1.getName()))
                .andExpect(jsonPath("$[0].color").value(FACULTY_1.getColor()))
                .andReturn().getResponse().getContentAsString();
        assertThat(GSON.fromJson(res, List.class).size()).isEqualTo(3);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color={color}", "Red")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(basicFaculty.getId()))
                .andExpect(jsonPath("$[0].name").value(basicFaculty.getName()))
                .andExpect(jsonPath("$[0].color").value(basicFaculty.getColor()));
    }

    @Test
    public void shouldGetCorrectFacultiesByName() throws Exception {
        List<Faculty> resultBlu = List.of(FACULTY_1, FACULTY_2);
        List<Faculty> resultRed = List.of(basicFaculty);
        when(facultyRepository.findFacultiesByName(eq("blu"))).thenReturn(resultBlu);
        when(facultyRepository.findFacultiesByName(eq("rd"))).thenReturn(resultRed);
        String res = mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?name={name}", "blu")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(FACULTY_1.getId()))
                .andExpect(jsonPath("$[0].name").value(FACULTY_1.getName()))
                .andExpect(jsonPath("$[0].color").value(FACULTY_1.getColor()))
                .andExpect(jsonPath("$[1].id").value(FACULTY_2.getId()))
                .andExpect(jsonPath("$[1].name").value(FACULTY_2.getName()))
                .andExpect(jsonPath("$[1].color").value(FACULTY_2.getColor()))
                .andReturn().getResponse().getContentAsString();
        assertThat(GSON.fromJson(res, List.class).size()).isEqualTo(2);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?name={name}", "rd")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(basicFaculty.getId()))
                .andExpect(jsonPath("$[0].name").value(basicFaculty.getName()))
                .andExpect(jsonPath("$[0].color").value(basicFaculty.getColor()));
    }

    @Test
    public void shouldGetCorrectFacultyStudents() throws Exception {
        List<Student> result = List.of(BASIC_STUDENT, BASIC_STUDENT_2);
        BASIC_STUDENT.setFaculty(basicFaculty);
        BASIC_STUDENT_2.setFaculty(basicFaculty);
        BASIC_STUDENT_2.setId(2L);
        when(facultyRepository.findByFacultyName(eq("Rd"))).thenReturn(result);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?facultyName={name}", "Rd")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(BASIC_STUDENT.getId()))
                .andExpect(jsonPath("$[0].name").value(BASIC_STUDENT.getName()))
                .andExpect(jsonPath("$[0].age").value(BASIC_STUDENT.getAge()))
                .andExpect(jsonPath("$[1].id").value(BASIC_STUDENT_2.getId()))
                .andExpect(jsonPath("$[1].name").value(BASIC_STUDENT_2.getName()))
                .andExpect(jsonPath("$[1].age").value(BASIC_STUDENT_2.getAge()));
    }
}