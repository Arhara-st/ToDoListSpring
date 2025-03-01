package org.spring.todolistspring.task.repository;

import org.spring.todolistspring.task.model.entity.Task;
import org.spring.todolistspring.task.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAll(Sort sort);
    Page<Task> findAllByStatus(TaskStatus status, PageRequest pageRequest);

}

