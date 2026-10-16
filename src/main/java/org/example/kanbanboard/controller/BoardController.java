package org.example.kanbanboard.controller;

import lombok.AllArgsConstructor;
import org.example.kanbanboard.dto.TaskDto;
import org.example.kanbanboard.exception.NoTaskFoundException;
import org.example.kanbanboard.model.Task;
import org.example.kanbanboard.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todo")
@AllArgsConstructor
public class BoardController {

    private final BoardService service;

    @PostMapping
    public Task addTask(@RequestBody TaskDto taskDto){
        return service.addTask(taskDto);
    }

    @GetMapping
    public List<Task> getAllTasks(){
        return service.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable String id) throws NoTaskFoundException {
        return service.getTaskById(id);
    }

    @PutMapping("/{id}")
    public Task updateTaskById(@PathVariable String id, @RequestBody TaskDto taskDto) throws NoTaskFoundException {
        return service.updateTaskById(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteTaskById(@PathVariable String id) throws NoTaskFoundException {
        return service.deleteTaskById(id);
    }
}
