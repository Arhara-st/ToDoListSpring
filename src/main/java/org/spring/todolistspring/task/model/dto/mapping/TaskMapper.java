package org.spring.todolistspring.task.model.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spring.todolistspring.task.model.dto.TaskDto;
import org.spring.todolistspring.task.model.entity.Task;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    //@Mapping(target = "id", ignore = true)
    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);
    List<TaskDto> toDto(List<Task> tasks);

    //@Mapping(target = "id", ignore = true)
    List<Task> toEntity(List<TaskDto> tasksDto);

}
