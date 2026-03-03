package uk.gov.hmcts.reform.dev.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.models.TaskStatus;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task task = new Task(
            "Create an API",
            "Urgent task",
            TaskStatus.TODO,
            LocalDateTime.of(2026, 3, 5, 18, 0)
        );

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Create an API"))
            .andExpect(jsonPath("$.description").value("Urgent task"))
            .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        Task savedTask = taskRepository.save(
            new Task(
                "Test task",
                "Check get all tasks",
                TaskStatus.TODO,
                LocalDateTime.of(2026, 3, 5, 18, 0)
            )
        );

        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(savedTask.getId()))
            .andExpect(jsonPath("$[0].title").value("Test task"))
            .andExpect(jsonPath("$[0].status").value("TODO"));
    }

    @Test
    void shouldUpdateTaskStatus() throws Exception {
        Task savedTask = taskRepository.save(
            new Task(
                "Update me",
                "Check patch endpoint",
                TaskStatus.TODO,
                LocalDateTime.of(2026, 3, 5, 18, 0)
            )
        );

        mockMvc.perform(patch("/tasks/" + savedTask.getId() + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"DONE\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(savedTask.getId()))
            .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task savedTask = taskRepository.save(
            new Task(
                "Delete me",
                "Check delete endpoint",
                TaskStatus.TODO,
                LocalDateTime.of(2026, 3, 5, 18, 0)
            )
        );

        mockMvc.perform(delete("/tasks/" + savedTask.getId()))
            .andExpect(status().isNoContent());
    }
}