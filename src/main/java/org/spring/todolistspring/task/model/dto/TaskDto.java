package org.spring.todolistspring.task.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.spring.todolistspring.task.model.enums.TaskStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TaskDto {

    @NotNull
    @Length(min = 1, max = 40)
    private String title;

    @NotNull
    @Length(min = 1, max = 40)
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    private TaskStatus status;
}
