package com.bibavix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
@Table(name = "priorities", catalog = "todo_list", schema = "todo_list")
public class Priority {
    @Id
    @GeneratedValue
    @Column(name = "priority_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    @JdbcTypeCode(Types.BINARY)
    private UUID priorityId;

    @Column(name = "priority")
    @Size(min = 4, max = 8, message = "Priority name should be between 3 and 8 characters.")
    private String priorityName;
}
