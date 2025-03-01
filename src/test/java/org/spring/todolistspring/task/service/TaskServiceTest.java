package org.spring.todolistspring.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.spring.todolistspring.task.model.dto.TaskDto;
import org.spring.todolistspring.task.model.dto.mapping.TaskMapper;
import org.spring.todolistspring.task.model.entity.Task;
import org.spring.todolistspring.task.model.enums.TaskStatus;
import org.spring.todolistspring.task.repository.TaskRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import java.util.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    private TaskDto taskDto1;
    private TaskDto taskDto2;
    private Task task1;
    private Task task2;
    private final int pageNumber = 0;
    private final int pageSize = 2;

    @Mock
    private TaskRepository taskRepository;

    @MockitoSpyBean
    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository, taskMapper);

        taskDto1 = new TaskDto("Title 1", "Description 1", LocalDate.parse("2023-08-10"), TaskStatus.DONE);
        taskDto2 = new TaskDto("Title 2", "Description 2", LocalDate.parse("2023-08-15"), TaskStatus.NEW_TASK);

        task1 = new Task(1L, "Title 1", "Description 1", LocalDate.parse("2023-08-10"), TaskStatus.DONE);
        task2 = new Task(2L, "Title 2", "Description 2", LocalDate.parse("2023-08-15"), TaskStatus.NEW_TASK);

        taskRepository.save(task1);
        taskRepository.save(task2);
    }

    @Test
    public void createTest() {
        Task task = taskMapper.toEntity(taskDto1);

        when(taskMapper.toEntity(taskDto1)).thenReturn(task);

        taskService.create(taskDto1);
        verify(taskRepository).save(task);
    }

    @Test
    public void updateTest() {
        Long id = 20L;
        Task task = taskMapper.toEntity(taskDto1);

        when(taskMapper.toEntity(taskDto1)).thenReturn(task);
        when(taskRepository.existsById(id)).thenReturn(true);
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        taskService.update(taskDto1, id);
        verify(taskRepository).save(task);
    }

    @Test
    public void getByIdTest() {
        Long id = 2L;
        Optional<TaskDto> expected = Optional.of(taskMapper.toDto(task1));

        when(taskRepository.findById(id).map(taskMapper::toDto)).thenReturn(Optional.of(taskDto1));
        when(taskRepository.findById(id)).thenReturn(Optional.of(task1));

        Optional<TaskDto> actual = taskRepository.findById(id).map(taskMapper::toDto);
        verify(taskRepository).findById(id);
        assertEquals(expected,actual);
    }

    @Test
    public void deleteTest() {
        Long id = 7L;

        when(taskRepository.existsById(id)).thenReturn(true);

        taskService.delete(id);
        verify(taskRepository).deleteById(id);
    }

    @Test
    public void getAll() {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<Task> tasks = List.of(task2, task1);
        Page<Task> taskPage = new PageImpl<>(tasks, pageRequest, tasks.size());

        when(taskRepository.findAll(pageRequest)).thenReturn(taskPage);
        when(taskMapper.toDto(tasks)).thenReturn(List.of(taskDto1, taskDto2));

        List<TaskDto> result = taskService.getAll(pageNumber, pageSize);

        assertNotNull(result);
        verify(taskRepository).findAll(pageRequest);

    }

    @Test
    public void getAllByDateTest() {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,Sort.by("date"));
        List<Task> tasks = List.of(task2, task1);
        Page<Task> taskPage = new PageImpl<>(tasks, pageRequest, tasks.size());
        List<TaskDto> expected = List.of(taskDto1, taskDto2);

        when(taskRepository.findAll(pageRequest)).thenReturn(taskPage);
        when(taskMapper.toDto(tasks)).thenReturn(List.of(taskDto1, taskDto2));

        List<TaskDto> result = taskService.getAllByDate(pageNumber, pageSize);

        assertNotNull(tasks);
        assertNotNull(result);
        assertEquals(expected,result);
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Title 2", result.get(1).getTitle());

        verify(taskRepository).findAll(pageRequest);
        verify(taskMapper, atLeastOnce()).toDto(tasks);
    }

    @Test
    public void getAllByStatusTest() {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<Task> tasks = List.of(task2, task1);
        Page<Task> taskPage = new PageImpl<>(tasks, pageRequest, tasks.size());
        List<TaskDto> expected = List.of(taskDto1);

        when(taskRepository.findAllByStatus(TaskStatus.DONE,pageRequest)).thenReturn(taskPage);
        when(taskMapper.toDto(tasks)).thenReturn(List.of(taskDto1));

        List<TaskDto> result = taskService.getAllByStatus(TaskStatus.DONE, pageNumber, pageSize);

        assertNotNull(tasks);
        assertNotNull(result);
        assertEquals(expected,result);
        assertEquals("Title 1", result.get(0).getTitle());

        verify(taskRepository).findAllByStatus(TaskStatus.DONE, pageRequest);
        verify(taskMapper, atLeastOnce()).toDto(tasks);
    }
}
