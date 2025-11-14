package com.bibavix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "priorities", catalog = "todo_list", schema = "todo_list")
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priority_id")
    private Integer priorityId;

    @Column(name = "priority")
    @Size(min = 4, max = 8, message = "Priority name should be between 3 and 8 characters.")
    private String priorityName;
}
