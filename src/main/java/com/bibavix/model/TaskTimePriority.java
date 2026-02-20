package com.bibavix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "task_time_priority", catalog = "todo_list", schema = "todo_list")
public class TaskTimePriority {
    @Id
    @GeneratedValue
    @Column(name = "time_priority_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    @JdbcTypeCode(Types.BINARY)
    private UUID taskTimePriorityId;

    @Column(name = "time")
    private Integer time;

    @Column(name = "priority_id")
    private UUID priorityId;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;
}
