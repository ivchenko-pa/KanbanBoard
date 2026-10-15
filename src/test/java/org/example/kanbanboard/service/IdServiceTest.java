package org.example.kanbanboard.service;

import org.example.kanbanboard.model.Status;
import org.example.kanbanboard.model.Task;
import org.example.kanbanboard.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IdServiceTest {

    @Test
    void nextId_shouldBeOne_whenNoTasksInRepo() {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);

        //WHEN
        when(mockRepo.count()).thenReturn(0L);
        int acteulNextId = new IdService(mockRepo).nextId();

        //Then
        assertEquals(1, acteulNextId);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 42})
    void nextId_shouldBeIncrementedByOne_whenAtLeastOneTasksInRepo(int value) {
        //GIVEN
        BoardRepository mockRepo = mock(BoardRepository.class);
        List<Task> tasksInRepo = new ArrayList<>();
        for (int i = 1; i<=value; i++){
            tasksInRepo.add(new Task(i,"Some task number: " + i, Status.OPEN));
        }

        //WHEN
        when(mockRepo.count()).thenReturn((long) value);
        when(mockRepo.findAll(Sort.by("id"))).thenReturn(tasksInRepo);
        int actualNextId = new IdService(mockRepo).nextId();

        //THEN
        assertEquals(value+1, actualNextId);
    }
}