package org.example.kanbanboard.service;

import org.example.kanbanboard.dto.TaskDto;
import org.example.kanbanboard.model.Task;
import org.example.kanbanboard.model.Status;
import org.example.kanbanboard.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class KanbanBoardServiceTest {


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
        Task expectedTask = new Task(1,"Some task", Status.OPEN);

        assertEquals(expectedTask, actualTask);
        verify(mockRepo, times(1)).save(expectedTask);
    }
}