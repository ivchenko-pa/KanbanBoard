package org.example.kanbanboard.service;

import lombok.AllArgsConstructor;
import org.example.kanbanboard.dto.TaskDto;
import org.example.kanbanboard.model.Status;
import org.example.kanbanboard.model.Task;
import org.example.kanbanboard.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository repo;
    private final IdService idService;

    public Task addTask(TaskDto taskDto) {

        Task task = Task.builder()
                .id(idService.nextId())
                .description(taskDto.description())
                .status(taskDto.status())
                .build();
        repo.save(task);
        return task;
    }

    public List<Task> getAllTasks(){
        return repo.findAll();
    }
}
