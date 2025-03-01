package org.spring.todolistspring.task.service;

import org.spring.todolistspring.commons.exception.NotFoundException;
import org.spring.todolistspring.task.model.dto.TaskDto;
import org.spring.todolistspring.task.model.dto.mapping.TaskMapper;
import org.spring.todolistspring.task.model.entity.Task;
import org.spring.todolistspring.task.model.enums.TaskStatus;
import org.spring.todolistspring.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional
    public void create(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        taskRepository.save(task);
    }

    public Optional<TaskDto> getById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDto);
    }


    @Transactional
    public void update(TaskDto taskDto, Long id) {
        Task task = taskMapper.toEntity(taskDto);
        task.setId(id);
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found", HttpStatus.BAD_REQUEST);
        } else taskRepository.findById(task.getId()).ifPresent(updatedTask -> {
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setDate(task.getDate());
            updatedTask.setStatus(task.getStatus());
            taskRepository.save(updatedTask);
        });
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found", HttpStatus.BAD_REQUEST);
        } else taskRepository.deleteById(id);
    }

    public List<TaskDto> getAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Task> taskPage = taskRepository.findAll(pageRequest);
        System.out.println(taskPage.getContent());
        return taskMapper.toDto(taskPage.getContent());
    }

    public List<TaskDto> getAllByDate(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,Sort.by("date"));
        Page<Task> taskPage = taskRepository.findAll(pageRequest);
        return taskMapper.toDto(taskPage.getContent());
    }

    public List<TaskDto> getAllByStatus(TaskStatus status, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Task> taskPage = taskRepository.findAllByStatus(status, pageRequest);
        return  taskMapper.toDto(taskPage.getContent());
    }
}
