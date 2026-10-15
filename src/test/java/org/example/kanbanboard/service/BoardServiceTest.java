package org.example.kanbanboard.service;

import org.example.kanbanboard.dto.TaskDto;
import org.example.kanbanboard.model.Status;
import org.example.kanbanboard.model.Task;
import org.example.kanbanboard.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BoardServiceTest {


    @Test
    void addTask_shouldBeReturnedTask_whenValidTaskDto() {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);
        TaskDto taskDto = new TaskDto("Some task", Status.OPEN);

        //WHEN
        when(mockIdService.nextId()).thenReturn(1);
        Task actualTask = service.addTask(taskDto);

        //THEN
        Task expectedTask = new Task(1, "Some task", Status.OPEN);

        assertEquals(expectedTask, actualTask);
        verify(mockRepo, times(1)).save(expectedTask);
    }

    @Test
    void getAllTasks_shouldBeEmptyList_whenNothingInDatabase() {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);
        List<Task> expectedList = Collections.emptyList();

        //WHEN
        when(mockRepo.findAll()).thenReturn(Collections.emptyList());
        List<Task> actualList = service.getAllTasks();

        //THEN
        assertEquals(expectedList, actualList);
        verify(mockRepo).findAll();
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 42})
    void getAllTasks_shouldListWithTask_whenThereAreTasksInDatabase(int value) {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);

        List<Task> tasksInDatabaseForMocKRepo = new ArrayList<>();
        List<Task> expectedListOfTasks = new ArrayList<>();

        for (int i = 1; i <= value; i++) {
            Task task = new Task(1, "Description: " + i, Status.OPEN);
            tasksInDatabaseForMocKRepo.add(task);
            expectedListOfTasks.add(task);
        }

        //WHEN
        when(mockRepo.findAll()).thenReturn(tasksInDatabaseForMocKRepo);
        List<Task> actualList = service.getAllTasks();

        //THEN
        assertEquals(expectedListOfTasks, actualList);
        verify(mockRepo).findAll();
    }
}