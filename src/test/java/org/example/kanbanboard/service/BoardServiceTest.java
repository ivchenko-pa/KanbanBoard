package org.example.kanbanboard.service;

import org.example.kanbanboard.dto.TaskDto;
import org.example.kanbanboard.exception.NoTaskFoundException;
import org.example.kanbanboard.model.Status;
import org.example.kanbanboard.model.Task;
import org.example.kanbanboard.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
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
        verify(mockRepo, times(1)).findAll();
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
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void getTaskById_shouldBeOneTask_WhenOneExist() throws NoTaskFoundException {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);
        Task expectedTask = new Task(1, "Some task", Status.OPEN);
        //WHEN
        when(mockRepo.findById(1)).thenReturn(Optional.of(new Task(1, "Some task", Status.OPEN)));
        Task actualTask = service.getTaskById("1");

        //THEN
        assertEquals(expectedTask, actualTask);
        verify(mockRepo, times(1)).findById(1);

    }

    @Test
    void getTaskById_shouldBeException_WhenTaskNotExist() {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);
        //WHEN
        when(mockRepo.findById(1)).thenReturn(Optional.empty());
        try {
            service.getTaskById("1");
            //THEN
            fail("Expected Exception not thrown, even though task with id: 1, that does not exist.");
        } catch (NoTaskFoundException e) {
            assertEquals("Task with id: 1 doesn't exist.", e.getMessage());
        }
    }

    @Test
    void updateTaskById_shouldBeReturnedOne_whenOneExists() throws NoTaskFoundException {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);
        TaskDto inputTaskDto = new TaskDto("Some task", Status.IN_PROGRESS);
        Task expectedTask = new Task(1, "Some task", Status.IN_PROGRESS);
        when(mockRepo.existsById(1)).thenReturn(true);

        //WHEN
        Task actualTask = service.updateTaskById("1", inputTaskDto);

        //THEN
        assertEquals(expectedTask, actualTask);
        verify(mockRepo, times(1)).save(expectedTask);
    }

    @Test
    void updateTaskById_shouldBeException_whenNoneExists() {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);
        TaskDto inputTaskDto = new TaskDto("Some task", Status.IN_PROGRESS);
        when(mockRepo.existsById(1)).thenReturn(false);
        //WHEN
        try {
            service.updateTaskById("1", inputTaskDto);
            fail("Expected Exception not thrown, even though task with id: 1, that does not exist.");
        } catch (NoTaskFoundException e) {
            assertEquals("Task with id: 1 doesn't exist.", e.getMessage());
        }
    }

    @Test
    void deleteTaskById_shouldBeTrue_whenTaskExist() {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);

        when(mockRepo.existsById(1)).thenReturn(true);

        //WHEN
        boolean actualResult = service.deleteTaskById("1");

        //THEN
        assertEquals(true, actualResult);
        verify(mockRepo, times(1)).existsById(1);
        verify(mockRepo, times(1)).deleteById(1);
    }

    @Test
    void deleteTaskById_shouldBeFalse_whenNoneTaskExist() {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);

        when(mockRepo.existsById(1)).thenReturn(false);

        //WHEN
        boolean actualResult = service.deleteTaskById("1");

        //THEN
        assertEquals(false, actualResult);
        verify(mockRepo, times(1)).existsById(1);
        verify(mockRepo, times(0)).deleteById(1);
    }

    @Test
    void deleteTaskById_shouldBeFalse_whenIdIsNull() {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        IdService mockIdService = mock(IdService.class);
        BoardService service = new BoardService(mockRepo, mockIdService);

        //WHEN
        boolean actualResult = service.deleteTaskById(null);

        //THEN
        assertEquals(false, actualResult);
        verify(mockRepo, times(0)).existsById(1);
        verify(mockRepo, times(0)).deleteById(1);
    }
}