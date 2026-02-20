package com.bibavix.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "task_status", catalog = "todo_list", schema = "todo_list")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatus {
    @Id
    @GeneratedValue
    @Column(name = "status_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    @JdbcTypeCode(Types.BINARY)
    private UUID statusId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;
}