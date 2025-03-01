package org.spring.todolistspring.task.controller;

import lombok.extern.slf4j.Slf4j;
import org.spring.todolistspring.task.model.dto.TaskDto;
import org.spring.todolistspring.task.model.enums.TaskStatus;
import org.spring.todolistspring.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //конкретная задача
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        var taskDto = taskService.getById(id);
        return taskDto.isEmpty() ? ResponseEntity.badRequest().body("Task not found")
                : ResponseEntity.ok(taskDto);
    }

    //создание новой задачи
    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody TaskDto taskDto) {
        taskService.create(taskDto);
        return ResponseEntity.ok("Task created");
    }

    //обновление задачи
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody TaskDto taskDto, @PathVariable Long id) {
        taskService.update(taskDto, id);
        return ResponseEntity.ok("Task updated");
    }

    //удаление задачи
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.delete(id);
        return   ResponseEntity.ok("Task deleted");
    }

    //получение всех задач
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        List<TaskDto> tasks = taskService.getAll(page, size);
        System.out.println(tasks.toString());
        return tasks.isEmpty() ? ResponseEntity.badRequest().body("No task found")
                : ResponseEntity.ok(tasks);
    }

    //сортировка всех задач по дате и вывод
    @GetMapping("/all/sorted")
    public ResponseEntity<?> getAllSortedByDate(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size) {
        return taskService.getAllByDate(page, size)
                .isEmpty() ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList())
                : ResponseEntity.ok(taskService.getAllByDate(page, size));
    }

    //фильтр по статусу задач
    @GetMapping("/all/filter/{status}")
    public ResponseEntity<?> getAllByStatus(@PathVariable TaskStatus status,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size) {
        List<TaskDto> tasks = taskService.getAllByStatus(status, page, size);
        return tasks.isEmpty() ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No task found")
                : ResponseEntity.ok(tasks);
    }
}
// из сервиса
//потетря соединения  с бд
//хандле р