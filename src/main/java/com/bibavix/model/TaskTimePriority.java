package com.bibavix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "task_time_priority", catalog = "todo_list", schema = "todo_list")
public class TaskTimePriority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_priority_id")
    private Integer taskTimePriorityId;

    @Column(name = "time")
    private Date time;

    @Column(name = "priority_id")
    private Integer priorityId;
}
