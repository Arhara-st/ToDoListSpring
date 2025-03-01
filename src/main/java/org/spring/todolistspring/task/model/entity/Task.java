package org.spring.todolistspring.task.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.spring.todolistspring.task.model.enums.TaskStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // идентификатор задачи

    @Column(name = "title")
    private String title; // имя задачи

    @Column(name = "description")
    private String description; // описание задачи

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date; // срок выполнения

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status; // статус задачи
}
