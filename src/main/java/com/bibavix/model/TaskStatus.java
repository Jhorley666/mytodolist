package com.bibavix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "task_status", catalog = "todo_list", schema = "todo_list")
@Getter
@Setter
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Short statusId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;
}