package org.example.kanbanboard.controller;

import jakarta.servlet.ServletException;
import org.example.kanbanboard.model.Status;
import org.example.kanbanboard.model.Task;
import org.example.kanbanboard.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository repo;

    @Test
    void getAllTasks_shouldReturnListWithTwoTasks_whenTwoExist() throws Exception {
        //GIVEN
        Task taskOne = new Task(1, "Some task One", Status.OPEN);
        Task taskTwo = new Task(2, "Some task Two", Status.IN_PROGRESS);
        repo.saveAll(List.of(taskOne, taskTwo));

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [   {
                                 "id": 1,
                                 "description": "Some task One",
                                 "status": "OPEN"
                             },
                             {
                                 "id": 2,
                                 "description": "Some task Two",
                                 "status": "IN_PROGRESS"
                             }]
                        """));

    }

    @Test
    void getAllTasks_shouldReturnEmptyList_whenNoneExist() throws Exception {
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    void getTaskById_shouldReturnOneTask_whenThisExist() throws Exception {
        //GIVEN
        Task task = new Task(1, "Some task", Status.DONE);
        repo.save(task);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/1"))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                          "id": 1,
                          "description": "Some task",
                          "status": "DONE"
                        }
                        """));
    }

    @Test
    void getTaskById_shouldReturnOneTask_whenManyIncludingThisExist() throws Exception {
        //GIVEN
        Task taskOne = new Task(1, "Some task One", Status.OPEN);
        Task taskTwo = new Task(2, "Some task Two", Status.IN_PROGRESS);
        Task taskThree = new Task(3, "Some task Three", Status.DONE);
        repo.saveAll(List.of(taskOne, taskTwo, taskThree));

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/2"))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                          "id": 2,
                          "description": "Some task Two",
                          "status": "IN_PROGRESS"
                        }
                        """));
    }

    @Test
    void getTaskById_shouldReturnNull_whenManyExistAndThisNotExist() throws Exception {
        //GIVEN
        Task taskOne = new Task(1, "Some task One", Status.OPEN);
        Task taskThree = new Task(3, "Some task Three", Status.DONE);
        repo.saveAll(List.of(taskOne, taskThree));

        //WHEN
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/2"));
            //THEN
            fail("Expected Exception not thrown, even though task with id: 2, that does not exist.");
        } catch (ServletException e) {
            assertEquals("Task with id: 2 doesn't exist.", e.getCause().getMessage());
        }
    }

    @Test
    void addTask_shouldBeReturnedTask_whenValidProvided() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "description": "Some task",
                                    "status": "OPEN"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                          "id": 1,
                          "description": "Some task",
                          "status": "OPEN"
                        }
                        """));
    }

    @Test
    void deleteTaskById_shouldReturnTrue_whenThisExist() throws Exception {
        //GIVEN
        Task task = new Task(1, "Some task", Status.DONE);
        repo.save(task);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/1"))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void deleteTaskById_shouldReturnFalse_whenThisNotExist() throws Exception {
        //GIVEN
        Task taskOne = new Task(1, "Some task One", Status.OPEN);
        Task taskThree = new Task(3, "Some task Three", Status.DONE);
        repo.saveAll(List.of(taskOne, taskThree));

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/2"))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }
}