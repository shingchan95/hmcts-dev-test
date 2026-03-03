package uk.gov.hmcts.reform.dev.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskFlowFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void shouldCompleteTaskLifecycle() throws Exception {
        String createResponse = mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title": "Functional test task",
                      "description": "Full task lifecycle",
                      "status": "TODO",
                      "dueDateTime": "2026-03-05T18:00:00"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Functional test task"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode createdTask = objectMapper.readTree(createResponse);
        long taskId = createdTask.get("id").asLong();

        mockMvc.perform(get("/tasks/" + taskId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(taskId))
            .andExpect(jsonPath("$.status").value("TODO"));

        mockMvc.perform(patch("/tasks/" + taskId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"DONE\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DONE"));

        mockMvc.perform(delete("/tasks/" + taskId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/" + taskId))
            .andExpect(status().isNotFound());
    }
}