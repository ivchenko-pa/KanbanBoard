package org.example.kanbanboard.controller;

import lombok.AllArgsConstructor;
import org.example.kanbanboard.dto.TaskDto;
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

}
